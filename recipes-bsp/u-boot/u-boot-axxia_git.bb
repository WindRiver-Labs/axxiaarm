#
# Copyright (C) 2014 Wind River Systems, Inc.
#
require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

# This revision corresponds to the tag "u-boot_v2015.10_axxia_1.96"
SRCREV = "b939d8c53af5f137ba095fe1666b1064dc948613"

UBOOT_MACHINE_axxiaarm64 = "axm5600_defconfig"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

COMPATIBLE_MACHINE = "^axxiaarm64$"
SRC_URI = "git://github.com/axxia/axxia_u-boot.git;branch=axxia-dev"

SRC_URI_append = " file://0005-lsi-Add-the-Ability-to-Build-with-GCC6.patch \
			   file://0001-axm56xx-fix-the-u-boot-compile-fail.patch \
			   file://0001-ARMv8-add-GOT-sections-to-the-list-of-sections-copie.patch \
			   file://0001-axm5600-correct-MKIMAGEFLAGS-of-u-boot.img-and-u-boo.patch \
		 "
DEPENDS_append = " atf-axxia"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
UBOOT_MAKE_TARGET ?= "all"

EXTRA_OEMAKE += "AXXIA_VERSION=u-boot_v2015.10_axxia_1.96 AXXIA_ATF_VERSION=atf_84091c4_axxia_1.43"
PV = "2013.01.01+git${SRCREV}"

# Some versions of u-boot use .bin and others use .img.  By default use .bin
# but enable individual recipes to change this value.
UBOOT_SUFFIX = "img"
UBOOT_IMAGE = "u-boot-${MACHINE}-${PV}-${PR}.${UBOOT_SUFFIX}"
UBOOT_BINARY = "u-boot.${UBOOT_SUFFIX}"
UBOOT_SYMLINK = "u-boot-${MACHINE}.${UBOOT_SUFFIX}"

# SPL (Secondary Program Loader)
SPL_SUFFIX = "img"
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

	install -d ${B}/spl
	cp ${STAGING_LIBDIR}/bl31.o ${B}/spl/bl31.o
	oe_runmake -C ${S} O=${B} ${UBOOT_MAKE_TARGET}
	${B}/tools/mkimage -A arm64 -T firmware -C none -a 0 -e 0x00187001 -n u-boot-spl -d ${B}/spl/u-boot-spl.bin ${B}/spl/u-boot-spl.img
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

