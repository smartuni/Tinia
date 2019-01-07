APPLICATION = weather-station

BOARD ?= nucleo-f401re
RIOTBASE ?= ${HOME}/Tutorials/RIOT

FEATURES_REQUIRED += periph_gpio
FEATURES_REQUIRED += periph_gpio_irq
FEATURES_REQUIRED += periph_rtc
FEATURES_REQUIRED += periph_adc
USEMODULE += xtimer
USEMODULE += shell
USEMODULE += shell_commands
USEMODULE += ps
USEMODULE += $(DRIVER)
USEMODULE += fmt


DEVEUI ?= 0011223344556677
APPEUI ?= 70B3D57ED0012D5E
APPKEY ?= 0DF95E23D554D0A6F9103857C65A9C48

# Default radio driver is Semtech SX1276 (used by the B-L072Z-LRWAN1 board)
DRIVER ?= sx1272

# Default region is Europe and default band is 868MHz
REGION ?= EU868

# Include the Semtech-loramac package
USEPKG += semtech-loramac

# Include Cayenne-lpp package
USEPKG += cayenne-lpp

CFLAGS += -DREGION_$(REGION)
CFLAGS += -DDEVEUI=\"$(DEVEUI)\" -DAPPEUI=\"$(APPEUI)\" -DAPPKEY=\"$(APPKEY)\"
CFLAGS += -DLORAMAC_ACTIVE_REGION=LORAMAC_REGION_$(REGION)

include $(RIOTBASE)/Makefile.include
