/**
 * 
 */

		//activate hamburger-bar


	
		//activate Enterkey for Search
			var searchWord = document.getElementById('searchWord');
			var searchButton = document.getElementsByClassName('searchButton');
			searchWord.addEventListener('keyup', function(event) {
				
				if (event.keyCode === null) {
					event.preventDefault();
				}
				
				if (event.keyCode === 13) {
					
					window.location.href = searchButton.href;
				}
			});