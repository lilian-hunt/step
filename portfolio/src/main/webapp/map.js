const jacksonHole = {
  lat: 43.582767,
  lng: -110.821999
};
const LA = {
  lat: 34.052235,
  lng: -118.243683
};

const jacksonHoleContentString =
    'One of my favourite places to ski! The challenging terrain and magnificent views are remarkable!';
const LAContentString =
    'Shopping in Beverly Hills, watching basketball games, and visiting Santa Monica pier is always fun.';


/** Helper function to add location markers to map. */
function addMarkers(map) {
  const infowindowJacksonHole = new google.maps.InfoWindow({
    content: jacksonHoleContentString,
  });

  const infowindowLA = new google.maps.InfoWindow({
    content: LAContentString,
  });

  const markerJacksonHole = new google.maps.Marker({
    position: jacksonHole,
    map: map,
  });

  const markerLA = new google.maps.Marker({
    position: LA,
    map: map,
  });

  markerJacksonHole.addListener('click', () => {
    infowindowJacksonHole.open(map, markerJacksonHole);
  });

  markerLA.addListener('click', () => {
    infowindowLA.open(map, markerLA);
  });
}

/** Toggle night mode and day mode of map. */
function toggleMapType() {
  const button = document.getElementById('toggle-map');
  if (button.innerText == 'Night Mode') {
    nightMode();
    button.innerText = 'Day Mode';
  } else {
    createMap();
    button.innerText = 'Night Mode';
  }
}
/** Creates a day mode map and adds it to the page. */
function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'), {center: jacksonHole, zoom: 4});
  addMarkers(map);
}

/** Creates a night mode version of the map */
function nightMode() {
  const map = new google.maps.Map(document.getElementById('map'), {
    center: jacksonHole,
    zoom: 4,
    styles: [
      {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
      {
        featureType: 'administrative.locality',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}],
      },
      {
        featureType: 'poi',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}],
      },
      {
        featureType: 'poi.park',
        elementType: 'geometry',
        stylers: [{color: '#263c3f'}],
      },
      {
        featureType: 'poi.park',
        elementType: 'labels.text.fill',
        stylers: [{color: '#6b9a76'}],
      },
      {
        featureType: 'road',
        elementType: 'geometry',
        stylers: [{color: '#38414e'}],
      },
      {
        featureType: 'road',
        elementType: 'geometry.stroke',
        stylers: [{color: '#212a37'}],
      },
      {
        featureType: 'road',
        elementType: 'labels.text.fill',
        stylers: [{color: '#9ca5b3'}],
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry',
        stylers: [{color: '#746855'}],
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry.stroke',
        stylers: [{color: '#1f2835'}],
      },
      {
        featureType: 'road.highway',
        elementType: 'labels.text.fill',
        stylers: [{color: '#f3d19c'}],
      },
      {
        featureType: 'transit',
        elementType: 'geometry',
        stylers: [{color: '#2f3948'}],
      },
      {
        featureType: 'transit.station',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}],
      },
      {
        featureType: 'water',
        elementType: 'geometry',
        stylers: [{color: '#17263c'}],
      },
      {
        featureType: 'water',
        elementType: 'labels.text.fill',
        stylers: [{color: '#515c6d'}],
      },
      {
        featureType: 'water',
        elementType: 'labels.text.stroke',
        stylers: [{color: '#17263c'}],
      },
    ],
  });
  addMarkers(map);
}