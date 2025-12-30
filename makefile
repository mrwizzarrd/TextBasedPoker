JAVAC = javac
JAVA = java
BUILD_DIR = build
MAIN_CLASS = poker.Game
SRC_DIR = src

JAVA_SOURCES := $(wildcard $(SRC_DIR)/poker/*.java)

.PHONY: all clean run

all: $(BUILD_DIR)/.compiled

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

$(BUILD_DIR)/.compiled: $(JAVA_SOURCES) | $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(JAVA_SOURCES)
	touch $@

run: all
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)

clean:
	rm -rf $(BUILD_DIR)

