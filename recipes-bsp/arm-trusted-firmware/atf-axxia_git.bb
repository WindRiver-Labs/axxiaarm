include arm-trusted-firmware.inc

# This revision corresponds to the tag "atf_84091c4_axxia_1.31"
SRCREV ?= "159d17501567482a54f8425fc1383ee7f77e1dcb"

PV = "1.2-axxia+git${SRCREV}"

SRC_URI_append = " file://0001-context_mgmt.h-fix-compile-error.patch"
