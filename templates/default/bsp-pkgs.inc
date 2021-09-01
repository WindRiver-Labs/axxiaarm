IMAGE_INSTALL += "${@bb.utils.contains("MACHINE_FEATURES", "bootloader-axxia", "u-boot-axxia", " ", d)}"
