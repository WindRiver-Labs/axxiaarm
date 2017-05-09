TARGET_SUPPORTED_KTYPES_axxiaarm = "standard preempt-rt cgl"
KERNEL_DEVICETREE_axxiaarm = "axm5516-amarillo.dtb \
				axm5516-sim.dtb"
TARGET_SUPPORTED_KTYPES_axxiaarm64 = "standard preempt-rt cgl"
KERNEL_DEVICETREE_axxiaarm64 = "intel/axm5616-sim.dtb \
				intel/axm5616-victoria.dtb"
KBRANCH_DEFAULT_standard_axxiaarm ?= "standard/${KMACHINE}"
KBRANCH_DEFAULT_preempt-rt_axxiaarm ?= "standard/preempt-rt/${KMACHINE}"
KBRANCH_DEFAULT_cgl_nxp-axxiaarm ?= "standard/cgl/${KMACHINE}"
KBRANCH_DEFAULT_standard_axxiaarm64 ?= "standard/${KMACHINE}"
KBRANCH_DEFAULT_preempt-rt_axxiaarm64 ?= "standard/preempt-rt/${KMACHINE}"
KBRANCH_DEFAULT_cgl_nxp-axxiaarm64 ?= "standard/cgl/${KMACHINE}"
