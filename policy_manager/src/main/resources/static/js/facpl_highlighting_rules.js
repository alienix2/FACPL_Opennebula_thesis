define(function(require, exports, module) {
    "use strict";

    var oop = require("../lib/oop");
    var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

    var FacplHighlightRules = function() {
        this.$rules = {
            "start": [
                {
                    token: "comment",
                    regex: /\/\/.*/,
                },
                {
                    token: "string",
                    regex: /"[^"]*"/,
                },
                {
                    token: "constant.numeric",
                    regex: /\b\d+(\.\d+)?\b/,
                },
                {
                    token: "keyword",
                    regex: /\b(?:import|if|else|for|while|return)\b/,
                },
                {
                    token: "variable.language",
                    regex: /\b(?:true|false|null)\b/,
                },
                {
                    token: "identifier",
                    regex: /[a-zA-Z_][a-zA-Z0-9_]*/,
                },
                {
                    token: "operator",
                    regex: /[+\-*\/=<>!&|]/,
                },
                {
                    token: "paren.lparen",
                    regex: /[({\[]/,
                },
                {
                    token: "paren.rparen",
                    regex: /[)}\]]/,
                }
            ]
        };
    };
    oop.inherits(FacplHighlightRules, TextHighlightRules);

    exports.FacplHighlightRules = FacplHighlightRules;
});
