# name of your application
APPLICATION = tinia

# default board:
BOARD ?= nucleo-f401re

# This has to be the absolute path to the RIOT base directory:
# RIOTBASE ?= $(CURDIR)/../..

BOARD_INSUFFICIENT_MEMORY := nucleo-f031k6 nucleo-f042k6 nucleo-l031k6

DEVEUI ?= XXXXXXXXXXXXXXXX
APPEUI ?= XXXXXXXXXXXXXXXX
APPKEY ?= XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

# Default radio driver is Semtech SX1276
DRIVER ?= sx1272

# Default region is Europe and default band is 868MHz
REGION ?= EU868

# Include the Semtech-loramac package
USEPKG += semtech-loramac

# Include Cayenne-lpp package
USEPKG += cayenne-lpp

# Include Timer package
USEMODULE += xtimer

USEMODULE += $(DRIVER)
USEMODULE += fmt
FEATURES_REQUIRED += periph_rtc
FEATURES_REQUIRED += periph_adc

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
