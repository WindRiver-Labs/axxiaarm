DESCRIPTION = "U-boot bootloader fw_printenv/setenv utils"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"
SECTION = "bootloader"
DEPENDS = "mtd-utils"

# This revision corresponds to the tag "lsi_axxia_u-boot_5.8.1.55"
SRCREV = "8bf8eca3223bba98e161b30fc6110d1edf0be9c2"

PV = "2013.01.01+git${SRCREV}"
COMPATIBLE_MACHINE = "^axxiaarm$"
SRC_URI = "git://github.com/lsigithub/lsi_axxia_uboot_public.git;branch=lsi-v2013.01.01"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'HOSTCC="${CC}" HOSTSTRIP="true"'

inherit uboot-config

do_compile () {
  oe_runmake ${UBOOT_MACHINE}_config
  oe_runmake env
}

do_install () {
  install -d ${D}${base_sbindir}
  install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
  install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_setenv
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
