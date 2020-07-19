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
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Reality can be whatever I want.',
      'The hardest choices require the strongest wills.',
      'Perfectly balanced, as all things should be.',
      'Reality is often disappointing.',
      'A small price to pay for salvation',
      ];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}
function getGreetingUsingArrowFunctions() {
  fetch('/data').then(response => response.json()).then((comments) => {
    console.log(comments);
    var html = "<ul>";
q
    for (const comment of comments) {
        html += `<li>${comment['email']}: ${comment['text']}</li>`;
    }
    html += "</ul>";
    document.getElementById('greeting-container').innerHTML = html;
    //document.getElementById('greeting-container').innerText = json;
    //console.log(quote);
  });
}
function getLoginStatus() {
  fetch('/home').then(response => response.text()).then((html) => {
    document.getElementById('login-container').innerHTML = html;

    if (document.getElementById('email-line').innerText != "Hello stranger.") {
        document.getElementById('form-container').style.display = "";
    } else {
        document.getElementById('form-container').style.display = "none";
    }

  });
}
function start(){
    getGreetingUsingArrowFunctions();
    getLoginStatus();
}