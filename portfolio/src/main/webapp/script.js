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
      'I have worked at Deloitte.', 'I have worked at Macqaurie bank.',
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
    const commentsEntries = comments.comments;

    const commentListElement = document.getElementById('comment-container');
    commentListElement.innerHTML = '';
    for (const [key, value] of Object.entries(commentsEntries)) {
      commentListElement.appendChild(createListElement(key, value));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(key, comment) {
  const liElement = document.createElement('li');
  liElement.id = key;
  liElement.innerText = comment.commentText + ',' + comment.userEmail + '\t';

   if ('imageUrl' in comment) {
    const img = document.createElement('img');
    img.src = comment.imageUrl;
    liElement.appendChild(img);
  }
  // Create an input element to delete item.
  const form = document.createElement('form');
  form.setAttribute('method', 'post');
  form.setAttribute('action', '/delete-data');

  // Create hidden element to store comment id (unique key used to 
  // identify the commment in the database) and link it to the button.
  const comment_id = document.createElement('input');
  comment_id.setAttribute('type', 'hidden');
  comment_id.setAttribute('name', 'comment_id');
  comment_id.setAttribute('value', key);

  // Create hidden element to store user email.
  const userEmail = document.createElement('input');
  userEmail.setAttribute('type', 'hidden');
  userEmail.setAttribute('name', 'userEmail');
  userEmail.setAttribute('value', comment.userEmail);

  const button = document.createElement('input');
  button.setAttribute('class', 'btn btn-outline-secondary');
  button.setAttribute('type', 'submit');
  button.setAttribute('name', key);
  button.setAttribute('value', 'X');

  // Create the list element
  form.appendChild(comment_id);
  form.appendChild(userEmail);
  form.appendChild(button);
  liElement.appendChild(form);

  return liElement;
}

// Check if the user is logged in or not, only display the comment function
// if they are logged in.
const xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
  if (this.readyState == 4 && this.status == 200) {
    const commentTitle = document.getElementById('comment-header');
    const loginButton = document.getElementById("login-button");
    if (xhttp.responseText.includes('<p>You are logged in!</p>')) {
      const form = document.createElement('form');
      form.setAttribute('method', 'POST');
      form.setAttribute('id', 'comment-form');
      form.setAttribute('class', 'hidden');
      form.setAttribute('enctype', 'multipart/form-data');

      const feedbackPrompt = document.createElement('p');
      feedbackPrompt.innerText = 'Enter your feedback here:';

      const textArea = document.createElement('textarea');
      textArea.name = 'text-input';
      textArea.required = true;

      const imgText = document.createElement('p');
      imgText.innerText = 'Upload an image:';

      const imgInput = document.createElement('input');
      imgInput.type = 'file';
      imgInput.name = 'image';
      imgInput.setAttribute('class', 'btn btn-outline-secondary');

      const breakElement = document.createElement('br');
      const breakElement2 = document.createElement('br');

      const button = document.createElement('input');
      button.setAttribute('class', 'btn btn-outline-secondary');
      button.setAttribute('type', 'submit');

      form.appendChild(feedbackPrompt);
      form.appendChild(textArea);
      form.appendChild(imgText);
      form.appendChild(imgInput);
      form.appendChild(breakElement);
      form.appendChild(breakElement2);
      form.appendChild(button);
      commentTitle.parentNode.insertBefore(form, commentTitle.nextSibling);

      loginButton.value = "Logout";
    } else {
      const text = document.createElement('p');
      text.innerHTML = 'Please login to comment.';
      commentTitle.append(text);
      commentTitle.parentNode.insertBefore(text, commentTitle.nextSibling);

      loginButton.value = "Login";
    }
  }
};
xhttp.open('GET', '/login', true);
xhttp.send();

// Load the API key from file and attach to html.
fetch('./config.json')
    .then(response => {
      return response.json();
    })
    .then(data => {
      const script = document.createElement('script');
      script.src =
          'https://maps.googleapis.com/maps/api/js?key=' + data.api_key;
      document.head.appendChild(script);
      const mapScript = document.createElement('script');
      mapScript.src = 'map.js'
      document.head.appendChild(mapScript);
    });

/**  Make a GET request to /blobstore-upload-url. */
function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-upload-url')
      .then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('comment-form');
        messageForm.action = imageUploadUrl;
        messageForm.classList.remove('hidden');
      });
}