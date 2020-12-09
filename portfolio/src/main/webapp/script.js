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
    function getFact(){
        const facts = ['I went viral on TikTok for ski dance videos.', 
            'I love baking.', 'My favourite chocolate is Caramilk.', 'I play water polo.', 
            "I went to Stanford University on exchange.", 
            "I have worked at Deloitte.", "I have worked at Macqaurie bank."];

        // Pick a random one.
        const fact = facts[Math.floor(Math.random() * facts.length)];
        return fact;
    }
  
  const factContainer = document.getElementById('fact-container');
  
  // Show or hide fact.  
  if (factContainer.innerHTML == "") {
        factContainer.innerHTML = getFact();
  }

  else {
    factContainer.innerHTML = "";
  }

}


/**
 * Fetches comments from the servers and adds them to the DOM.
 */
function getFeedback() {
  fetch('/data').then(response => response.json()).then((comments) => {
    // comments is an object, so we have to
    // reference its fields to create HTML content
    var comments = comments.comments;
    comments = comments.substring(1,comments.length-1);
    comments = comments.split(", ");
    console.log(comments);
    
    const commentListElement = document.getElementById('comment-container');
    commentListElement.innerHTML = '';
    
    for (i = 0; i < comments.length; i++) {
        commentListElement.appendChild(
        createListElement(comments[i]));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}