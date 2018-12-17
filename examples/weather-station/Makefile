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

include $(RIOTBASE)/Makefile.include
