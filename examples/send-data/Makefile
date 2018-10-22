# name of your application
APPLICATION = lorawan

# Use the ST B-L072Z-LRWAN1 board by default:
BOARD ?= nucleo-f401re

# This has to be the absolute path to the RIOT base directory:
RIOTBASE ?= $(CURDIR)/../..

BOARD_INSUFFICIENT_MEMORY := nucleo-f031k6 nucleo-f042k6 nucleo-l031k6

DEVEUI ?= 9876543898765434
APPEUI ?= 70B3D57ED0012D5E
APPKEY ?= 6D8DAABCAD4B9344AB07F98AB6D11B8E

# Default radio driver is Semtech SX1276 (used by the B-L072Z-LRWAN1 board)
DRIVER ?= sx1272

# Default region is Europe and default band is 868MHz
REGION ?= EU868

# Include the Semtech-loramac package
USEPKG += semtech-loramac

# Include Cayenne-lpp package
USEPKG += cayenne-lpp

USEMODULE += $(DRIVER)
USEMODULE += fmt
FEATURES_REQUIRED += periph_rtc

CFLAGS += -DREGION_$(REGION)
CFLAGS += -DDEVEUI=\"$(DEVEUI)\" -DAPPEUI=\"$(APPEUI)\" -DAPPKEY=\"$(APPKEY)\"
CFLAGS += -DLORAMAC_ACTIVE_REGION=LORAMAC_REGION_$(REGION)

# Comment this out to disable code in RIOT that does safety checking
# which is not needed in a production environment but helps in the
# development process:
DEVELHELP ?= 1

# Change this to 0 show compiler invocation lines by default:
QUIET ?= 1

include $(RIOTBASE)/Makefile.include
