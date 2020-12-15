// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random fact to the page.
 */
function revealFact() {
  // Helper function to get a random fact.
  function getFact() {
    const facts = [
      'I went viral on TikTok for ski dance videos.', 'I love baking.',
      'My favourite chocolate is Caramilk.', 'I play water polo.',
      'I went to Stanford University on exchange.',
      'I have worked at Deloitte.', 'I have worked at Macqaurie bank.'
    ];

    // Pick a random one.
    const fact = facts[Math.floor(Math.random() * facts.length)];
    return fact;
  }

  const factContainer = document.getElementById('fact-container');

  // Show or hide fact.
  if (factContainer.innerHTML == '') {
    factContainer.innerHTML = getFact();
  } else {
    factContainer.innerHTML = '';
  }
}


/**
 * Fetches comments from the servers and adds them to the DOM.
 */
function getFeedback() {
  fetch('/data').then(response => response.json()).then((comments) => {
    // comments is an object, so reference its fields to create HTML content
    var comments = comments.comments;
    console.log(comments);

    const commentListElement = document.getElementById('comment-container');
    commentListElement.innerHTML = '';
    for (const [key, value] of Object.entries(comments)) {
      commentListElement.appendChild(createListElement(key, value));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(key, text) {
  const liElement = document.createElement('li');
  liElement.id = key;
  liElement.innerText = text + '\t';

  // Create an input element to delete item
  var form = document.createElement('form');
  form.setAttribute('method', 'post');
  form.setAttribute('action', '/delete-data');

  // Create hidden element to store id of button
  var id = document.createElement('input');
  id.setAttribute('type', 'hidden');
  id.setAttribute('name', 'id');
  id.setAttribute('value', key);

  var button = document.createElement('input');
  button.setAttribute('class', 'btn btn-outline-secondary');
  button.setAttribute('type', 'submit');
  button.setAttribute('name', key);
  button.setAttribute('value', 'X');

  // Create the list element
  form.appendChild(id);
  form.appendChild(button);
  liElement.appendChild(form);

  return liElement;
}

// Check if the user is logged in or not, only display the comment function
// if they are logged in
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
  if (this.readyState == 4 && this.status == 200) {
    commentTitle = document.getElementById('comment-header');

    if (xhttp.responseText.includes('<p>You are logged in!</p>')) {
      var form = document.createElement('form');
      form.setAttribute('method', 'POST');
      form.setAttribute('action', '/data');

      var text = document.createElement('p');
      text.innerText = 'Enter your feedback here:';

      var textArea = document.createElement('textarea');
      textArea.name = 'text-input';
      textArea.required = '';

      var br = document.createElement('br');

      var button = document.createElement('input');
      button.setAttribute('class', 'btn btn-outline-secondary');
      button.setAttribute('type', 'submit');

      form.appendChild(text);
      form.appendChild(textArea);
      form.appendChild(br);
      form.appendChild(button);
      commentTitle.parentNode.insertBefore(form, commentTitle.nextSibling);
      console.log('Comments');
    } else {
      var text = document.createElement('p');
      text.innerHTML = 'Please login to comment.';
      commentTitle.append(text);
      commentTitle.parentNode.insertBefore(text, commentTitle.nextSibling);
      console.log('No comments');
    }
  }
};
xhttp.open('GET', '/login', true);
xhttp.send();