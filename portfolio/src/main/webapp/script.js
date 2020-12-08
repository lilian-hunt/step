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
 * Adds a  random fact to the page.
 */
function revealFact() {
    function getFact(){
        const facts = ['I went viral on TikTok for ski dance videos. See <a href="https://www.tiktok.com/en/">here.</a>', 
        'I love baking.', 'My favourite chocolate is Caramilk.', 'I play water polo.', 
        "I went to Stanford University on exchange.", 
        "I have worked at Deloitte.", "I have worked at Macqaurie bank."];

        // Pick a random greeting.
        const fact = facts[Math.floor(Math.random() * facts.length)];
        return fact;
    }
  
    // Add it to the page.
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
 * Fetches a message from the server and adds it to the DOM. 
 */
function getMsg() {
    fetch('/data').then(response => response.text()).then((msg) => {
        document.getElementById('msg-container').innerHTML = msg;
    });
}

/**
 * Fetches stats from the servers and adds them to the DOM.
 */
function getFeedback() {
  fetch('/data').then(response => response.json()).then((msgs) => {
    // msgs is an object, not a string, so we have to
    // reference its fields to create HTML content
    var messages = msgs.comments;
    messages = messages.substring(1,messages.length-1);
    messages = messages.split(", ");
    console.log(messages);
    
    const msgListElement = document.getElementById('comment-container');
    msgListElement.innerHTML = '';
    
    for (i = 0; i < messages.length; i++) {
        msgListElement.appendChild(
        createListElement(messages[i]));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}