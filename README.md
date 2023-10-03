Demo link: https://www.youtube.com/watch?v=weRGVzgatWI

Create an app movie according to document requirements, coding using Java, XML, and Android. Apply: MVVM, Tab Layout, Navigation View, Toolbar, Interface, Jetpack(Room, View Binding), Fragment, Retrofit2 and RxJava3 call API, Rx Android, Room, Recycle View and DiffUtils, Dialog Fragment, ViewPager2, Alarm Manager and broadcast receiver to push notification, Live Data, Singleton class, Constraint Layout, Multi Language...

Main function:
1. Open app visible Splach Screen in 3 seconds
2. Have option menu button, when click, display 4 menu: Favourite, Movies, Settings, About. Switch to the corresponding tab
3. Show list movie can be sorted as follow: Popular Movies, Top-rated Movies, Upcoming Movies, Nowplaying Movies
   - Visible using view types: Grid, Vertical List by using view types button
   - List display 20 movies loaded from API (20 item per each request).
   - User can pull down to refresh and pull up to loadmore
4. Can favorite movie. favorited movies visible in Favourite tab
   Using tablayout display 4 tab				
		1. Movies: display list movies			
		2. Favourite: display list movies added to favourite list			
			Have Badge icon for display number of favourite movies		
		3. Setting: display setting screen			
		4. About: information about this app			
5. The sidebar menu						
	Using navigation drawer layout					
	Display information about profile and reminder					
	Profile: display profile information saved in local DB, edit in EP001-EditProfile					
		1. Avatar: Image, display avatar that user selected, if not have, display default image				
		2. Profile name				
		3. Birthday, display icon in the left				
		4. Email, display icon in the left				
		5. Sex, display icon in the left				
		6. Edit button: click go to EP001-EditProfile				
		7. Display list reminder in localDB, if have no item, do not display, when > 2 record, please display 2 record first				
		8. Showall: display SR001-ShowAllReminders				
						
6. Change Mode Button						
	Add a button on top right of Navigation Bar. This button should have 2 images:					
		1. Gridview icon when displaying LIST view				
		2. Listview icon when displaying GRID view				
		3. Click button Gridview to change display mode to: GRID view mode				
		4. Click button Listview to change display mode to: LIST view mode				
7. Display movie list under grid layout						
		Using recycle view				
		1. When select GRID view mode, change the list display into GRID form (using UICollectionView)				
		2. This collectionview should be scrollable. When scrolling to the bottom, should have LOAD MORE function to load more page				
		3. When Pull from the top, should have Pull To Refresh function to refresh				
						
8. Collectionview Cell(recycle view + grid layout manager)						
	Using xml for design					
	Display information					
		1. Movie title				
		2. Movie poster (thumbail image), please using synchronized process for this play this image, should be cached in local db				
		3. Release date (YYYY/MM/DD)				
		4. Display rating of the movie x.x				
		5. Display icon for adult movie (if not adult movie, do not display)				
		6. Display favourite icon, can click to this icon for add/remove favourite item				
		7. Overview: display overview information about movie	

***Movie list
1. Arrow down icon																												
	Display an arrow down icon in title bar
2. Dropdown menu																												
	Using fragment to display a dropdown menu.																											
	When click to titlebar, display this dropdown menu and can select 4 movies list, when selected, reload list movies.


***Movie Detail
1. Cast & Crew list											
	Add event touch to Cast & Crew list, use UIPageViewController										
	When touch to an Image, display image as fullscreen and can swipe left/right to display next/previous images
2. Details Cast & crew											
	Using API to get details										
		api.themoviedb.org/3/person/{persionId}?api_key=e7631ffcb8e766993e5ec0c1f4245f93									
	Display image as correct aspect ratio										
3. See More link											
	When click See More link, display full text of Overview										
	Using scroll view for display text.										
	Set background black and transparent 50% for Overview										


***Switch profile
Add new button switch profile																											
1. Switch profile button																											
	When click, display list profile screen																										
		Can add new profile or select exist profile																									
		When changed profile, list reminder and favourite must be change depend on Profile Name		
2. New Profile																											
	When click, go to edit profile button for add new profile									
3. List profiles																											
	When click to a profile, back to previous screen and display profile that selected		

***Setting 
Setting table vie, which have 3 sections: Filter, Sort By and Number Of Loading																										
1. Filter section																										
	In Filter section, users can choose kind of Movies which API uses to load. This is single choice.																									
	After selecting a kind of movie, when user back to Tab [Movies List], title must be changed with selected Kind. 																									
		App calls coressponding API to load selected kind of movies																								
	Slider to filter movie by rating:																									
		If users choose movie rate from 6.5, in tab Movies List just show movie with rate higher or equal to 6.5																								
	Click to show Year Pikcer to filter movie by release year:																									
		If users choose release year from 1970, in tab Movies List just show movie with release year from 1970		
2. Sort section																										
	In Sort By section, Users can select option to sort Movies in tab [Movies List]																									
	After selecting a Sort Option, when user back to Tab [Movies List], movies should be sort by Release Date (Descending).																									
	Click on each row to select option. This is single choice				
3. Other setting																										
	In Number Of Loading section, users can choose how many page per each loading.																									
	Click to show keyboard to input number of page per loading.																									
	After changing, app uses this number to load page per each loading.																									
Add new language setting, can select English or Japan


***Edit profile
1. Cancel button		
	Click CANCEL button to go to HOME screen without saving	
2. Avartar (Custom view)		
	Click Avatar to open Android image Library and select 1 image to set avatar	
3. Done button		
	Click DONE button to SAVE and go to HOME screen 	
4. Birthday (Date picker)		
	When click, show DatePicker to select birthday	
5. Email (Edit view)		
6. Sex (Using toggle button)


***ShowAllReminders
This screen display Table view show all reminder in of user.	
Each cell should show Poster of movie and following information:	
- First line: Movie Title - Year - Rating	
- Second line: Date time of reminder	
When user Tap on cell, go to Movie detail screen:


***about
Using web view and loading this url https://www.themoviedb.org/about/our-history																			
Display as smartphone layout
