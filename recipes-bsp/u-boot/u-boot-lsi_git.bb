#
# Copyright (C) 2014 Wind River Systems, Inc.
#
require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

# This revision corresponds to the tag "lsi_axxia_u-boot_5.8.1.66"
SRCREV = "5664e4d265021759850b7002a7647393a7db0eea"

COMPATIBLE_MACHINE = "^axxiaarm$"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/lsigithub/lsi_axxia_uboot_public.git;branch=lsi-v2013.01.01"
SRC_URI_append = " file://0001-lsi-axm55xx-fix-the-u-boot-compile-fail.patch \
		   file://0002-board-lsi-Add-fdt_high-and-initrd_high-variables.patch \
		   file://Dont-remove-version.c-and-ensure-the-version.c-is-wr.patch \
		   file://0003-lsi-Add-the-Ability-to-Build-with-GCC5.patch \
		   file://0004-lsi-axm55xx-fix-building-error-to-matchup-compiler-g.patch \
		 "

S = "${WORKDIR}/git"

PV = "2013.01.01+git${SRCREV}"

# Some versions of u-boot use .bin and others use .img.  By default use .bin
# but enable individual recipes to change this value.
UBOOT_SUFFIX = "img"
UBOOT_IMAGE = "u-boot-${MACHINE}-${PV}-${PR}.${UBOOT_SUFFIX}"
UBOOT_BINARY = "u-boot.${UBOOT_SUFFIX}"
UBOOT_SYMLINK = "u-boot-${MACHINE}.${UBOOT_SUFFIX}"

# SPL (Secondary Program Loader)
SPL_SUFFIX = "bin"
SPL_FILE = "u-boot-spl"
SPL_PATH = "spl"
SPL_BINARY = "${SPL_FILE}.${SPL_SUFFIX}"
SPL_IMAGE = "${SPL_FILE}-${MACHINE}-${PV}-${PR}.${SPL_SUFFIX}"
SPL_SYMLINK = "${SPL_FILE}-${MACHINE}.${SPL_SUFFIX}"

do_install () {
    install -d ${D}/boot
    install ${S}/${UBOOT_BINARY} ${D}/boot/${UBOOT_IMAGE}
    ln -sf ${UBOOT_IMAGE} ${D}/boot/${UBOOT_BINARY}

    if [ -e ${WORKDIR}/fw_env.config ] ; then
        install -d ${D}${sysconfdir}
        install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
    fi

    if [ "x${SPL_BINARY}" != "x" ]
    then
        install ${S}/${SPL_PATH}/${SPL_BINARY} ${D}/boot/${SPL_IMAGE}
        ln -sf ${SPL_IMAGE} ${D}/boot/${SPL_BINARY}
    fi
}

do_deploy () {
    install -d ${DEPLOYDIR}
    install ${S}/${UBOOT_BINARY} ${DEPLOYDIR}/${UBOOT_IMAGE}

    cd ${DEPLOYDIR}
    rm -f ${UBOOT_BINARY} ${UBOOT_SYMLINK}
    ln -sf ${UBOOT_IMAGE} ${UBOOT_SYMLINK}
    ln -sf ${UBOOT_IMAGE} ${UBOOT_BINARY}

    if [ "x${SPL_BINARY}" != "x" ]
    then
        install ${S}//${SPL_PATH}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_IMAGE}
        rm -f ${DEPLOYDIR}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_SYMLINK}
        ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_FILE}.${SPL_SUFFIX}
        ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_SYMLINK}
    fi
}

