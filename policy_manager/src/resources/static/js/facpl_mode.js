// Define the custom mode
ace.define("ace/mode/custom_mode", ["require", "exports", "ace/lib/oop", "ace/mode/text"], function(require, exports, oop, TextMode) {
    "use strict";

    // Custom Mode Class
    var CustomMode = function() {
        this.$behaviour = new CustomBehaviour();
    };
    oop.inherits(CustomMode, TextMode);

    (function() {
        // Define the mode name
        this.$id = "ace/mode/custom_mode";
        this.$name = "Custom Mode";

        // Define the highlighting rules
        this.getNextLineIndent = function(state, line, tab) {
            return this.$getIndent(line);
        };

        this.$highlightRules = new ace.require("ace/mode/text_highlight_rules").TextHighlightRules({
            start: [
                {
                    token: "keyword",
                    regex: "\\b(?:console|log)\\b"
                },
                {
                    token: "string",
                    regex: '".*?"'
                }
            ]
        });

        // Define the behavior for code completion
        var CustomBehaviour = function() {};
        oop.inherits(CustomBehaviour, ace.require("ace/mode/text").TextBehaviour);
    }).call(CustomMode.prototype);

    exports.CustomMode = CustomMode;
});
