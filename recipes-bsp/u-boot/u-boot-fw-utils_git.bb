DESCRIPTION = "U-boot bootloader fw_printenv/setenv utils"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM_axxiaarm = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"
LIC_FILES_CHKSUM_axxiaarm64 = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"
SECTION = "bootloader"
DEPENDS = "mtd-utils"

# This revision corresponds to the tag "lsi_axxia_u-boot_5.8.1.88"
SRCREV_axxiaarm = "074d50cff3abc9fd04a87b74edf228d15a216bd8"
SRCREV_axxiaarm64 = "7125f93af0c0987228b9fb322213af04a36dda58"

PV = "2013.01.01+git${SRCREV}"
UBOOT_MACHINE_axxiaarm64 = "axm5600_defconfig"
SRC_URI_axxiaarm = "git://github.com/lsigithub/lsi_axxia_uboot_public.git;nobranch=1"
SRC_URI_axxiaarm64 = "git://github.com/axxia/axxia_u-boot.git;branch=axxia-dev"
SRC_URI_append = " file://0001-fw_env-fix-compile-error-of-fw_env.patch\
		 "

S = "${WORKDIR}/git"
INSANE_SKIP_${PN}_append = "already-stripped"
EXTRA_OEMAKE_axxiaarm = 'CROSS_COMPILE=${TARGET_PREFIX} HOSTCC="${CC}" HOSTSTRIP="true"'
EXTRA_OEMAKE_axxiaarm64 = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${TARGET_PREFIX}gcc ${TARGET_CC_ARCH} ${TOOLCHAIN_OPTIONS}"'

COMPATIBLE_HOST_axxiaarm64="aarch64"
inherit uboot-config
UBOOT_AXXIA_CONFIG_axxiaarm = "axxia-55xx_config"
UBOOT_AXXIA_CONFIG_axxiaarm64 = "axm5600_defconfig"

do_compile () {
  oe_runmake ${UBOOT_AXXIA_CONFIG}
  oe_runmake env
}

do_install () {
  install -d ${D}${base_sbindir}
  install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
  install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_setenv
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
