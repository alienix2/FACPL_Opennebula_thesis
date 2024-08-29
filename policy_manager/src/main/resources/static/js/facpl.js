// facpl.js
hljs.registerLanguage('facpl', function(hljs) {
  return {
    name: 'FACPL',
    case_insensitive: true,
    keywords: {
      keyword: 'import PAS dec-fun map Request PolicySet Rule include',
      built_in: 'true false',
      literal: 'Int Double Bool String DateTime Set IntLiteral DoubleLiteral BooleanLiteral StringLiteral DateLiteral TimeLiteral',
      type: 'Set',
      function: 'equal not-equal less-than less-than-or-equal greater-than greater-than-or-equal in addition subtract divide multiply'
    },
    contains: [
      hljs.QUOTE_STRING_MODE,
      hljs.C_NUMBER_MODE,
      {
        className: 'comment',
        begin: /\/\*/,
        end: /\*\//,
        relevance: 0
      },
      {
        className: 'comment',
        begin: /\/\//,
        end: /$/,
        relevance: 0
      }
    ]
  };
});

