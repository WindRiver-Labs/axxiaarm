DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.md;md5=829bdeb34c1d9044f393d5a16c068371"

PROVIDES = "virtual/arm-trusted-firmware"
SRC_URI[md5sum] = "a43d3f07023571af48a8729c31e0f652"
inherit deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS += "u-boot-mkimage-native"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git/build"

BRANCH = "axxia-dev"
SRC_URI = "git://github.com/axxia/axxia_atf.git;branch=${BRANCH}"

ATF_BASE_NAME ?= "${PN}-${PKGE}-${PKGV}-${PKGR}-${DATETIME}"
ATF_BASE_NAME[vardepsexclude] = "DATETIME"

COMPATIBLE_MACHINE = "^axxiaarm64$"
PLATFORM_axxiaarm64 = "axxia"
# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"
OUTPUT_DIR = "${B}/${PLATFORM}/debug"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure() {
	:
}

do_compile() {
	oe_runmake -C ${S} DEBUG=1 BUILD_BASE=${B} PLAT=${PLATFORM} USE_COHERENT_MEM=0 bl31
	${CROSS_COMPILE}objcopy -I binary -O elf64-littleaarch64 -B aarch64 --rename-section .data=.monitor ${OUTPUT_DIR}/bl31.bin ${OUTPUT_DIR}/bl31.o
	install -d ${STAGING_DIR}/atf
	install -m 0644 ${OUTPUT_DIR}/bl31.o ${STAGING_DIR}/atf/bl31.o
}

do_install() {
	:
}

do_deploy() {
}
addtask deploy before do_build after do_compile
