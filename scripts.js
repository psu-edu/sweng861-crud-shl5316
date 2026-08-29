document.addEventListener('DOMContentLoaded', function () {
  var form = document.getElementById('noteForm');
  var input = document.getElementById('note');
  var output = document.getElementById('output');

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    var val = input.value.trim();
    if (!val) {
      output.textContent = 'Please enter a note.';
      output.style.color = 'red';
      return;
    }
    // XSS Protection with textContent
    output.textContent = val;
  });
});
