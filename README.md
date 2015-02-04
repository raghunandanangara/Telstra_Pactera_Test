This Test covers following features...

1.	Task of "Lazy Downloads of images on demand" for Listview was accomplished using Google's Volley Library.
2.	I am not allocating space to show a Image when there is no URL in imageHref field as per specifications
3.	I have used 2 more images for displaying images in Listview a) If the image is being downloaded, then "no_image.png" is displayed until original image download is completed b) If original image has failed to download(Reason can be anything eg:403 HTTP error), then "error_image1.jpg" is displayed
4.	Title of the App is dynamically updated using parsed JSON data
5.	Pull to Refresh UI pattern used for the ListView
6.	Polished and Commented code where necessary

Project Dependent Libraries:

1.	Google’s Android Volley Library for Networking. Its useful features are
	a)	Google Supported Open Source library natively build for Android
	b)	Backward compatible(via Android Support Library)
	c)	Elegant processing, scheduling, caching, queuing and prioritization of Network requests
	d)	Support for request prioritization.
	e)	Strong ordering that makes it easy to correctly populate your UI with data fetched asynchronously from the network.
	f)	Parallel downloading of Images best suited for ListViews
2.	Google’s Open Source Java Library GSON to convert JSON to Java Objects used for parsing.
