#
# Copyright (C) 2014 Wind River Systems, Inc.
#
require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

# This revision corresponds to the tag "lsi_axxia_u-boot_5.8.1.90"
SRCREV = "fca362a4628b8383f83660b7b7651f3d405161af"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

COMPATIBLE_MACHINE = "^axxiaarm$"
SRC_URI = "git://github.com/axxia/axxia_u-boot.git;branch=lsi-v2013.01.01"

SRC_URI_append = " file://0001-lsi-axm55xx-fix-the-u-boot-compile-fail.patch \
		   file://0001-axxia-fix-compile-error.patch \
		   file://0008-compiler-.h-sync-include-linux-compiler-.h-with-Linu.patch \
		   file://0009-config.mk-add-Wno-address-of-packed-member-to-CFLAGS.patch \
		   file://0011-axxia-lowlevel-ensure-CVE-2017-5715-cover-all-cores.patch \
		   file://0001-improve-format-of-filesize-for-fatload.patch \
		   file://0001-cmd_test-implement-e-test-for-file-existence.patch \
		   file://0001-enable-fat-cmd-for-axxia-55xx.patch \
		   file://0001-Increate-CONFIG_SYS_MAXARGS-to-workaround-setenv-iss.patch \
		   file://0001-enable-shell-for-axxia-55xx.patch \
		   file://0001-fix-usb-write-failed.patch \
		   file://0001-fix-USB2.0-device-timeout-issue.patch \
		   file://0001-ext4fs-use-EXT2_BLOCK_SIZE-instead-of-fs-blksz.patch \
		   file://0001-ext4fs-fix-invalid-extent-block-error.patch \
		 "

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
UBOOT_MAKE_TARGET ?= "all"

EXTRA_OEMAKE += "AXXIA_VERSION=lsi_axxia_u-boot_5.8.1.90"
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

do_configure() {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS

	oe_runmake -C ${S} O=${B} ${UBOOT_MACHINE}
}

do_compile() {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS

	oe_runmake -C ${S} O=${B} ${UBOOT_MAKE_TARGET}
}

do_install () {
    install -d ${D}/boot
    install ${B}/${UBOOT_BINARY} ${D}/boot/${UBOOT_IMAGE}
    ln -sf ${UBOOT_IMAGE} ${D}/boot/${UBOOT_BINARY}

    if [ -e ${WORKDIR}/fw_env.config ] ; then
        install -d ${D}${sysconfdir}
        install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
    fi

    if [ "x${SPL_BINARY}" != "x" ]
    then
        install ${B}/${SPL_PATH}/${SPL_BINARY} ${D}/boot/${SPL_IMAGE}
        ln -sf ${SPL_IMAGE} ${D}/boot/${SPL_BINARY}
    fi
}

do_deploy () {
    install -d ${DEPLOYDIR}
    install ${B}/${UBOOT_BINARY} ${DEPLOYDIR}/${UBOOT_IMAGE}

    cd ${DEPLOYDIR}
    rm -f ${UBOOT_BINARY} ${UBOOT_SYMLINK}
    ln -sf ${UBOOT_IMAGE} ${UBOOT_SYMLINK}
    ln -sf ${UBOOT_IMAGE} ${UBOOT_BINARY}

    if [ "x${SPL_BINARY}" != "x" ]
    then
        install ${B}/${SPL_PATH}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_IMAGE}
        rm -f ${DEPLOYDIR}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_SYMLINK}
        ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_FILE}.${SPL_SUFFIX}
        ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_SYMLINK}
    fi
}

