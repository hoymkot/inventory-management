//一个应该常用的javascript string函数，不知为何没有直接提供
String.prototype.escapeHTML = function () {                                       
  return this.replace(/&/g,'&amp;').replace(/>/g,'&gt;').replace(/</g,'&lt;').replace(/"/g,'&quot;');
};