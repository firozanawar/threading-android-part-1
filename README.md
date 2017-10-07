# threading-android-part-1
This project demonstrate the downloading of images from internet in background using normal java thread with HttpURLConnection.

### Note:-
* URL is used because it has many in-built methods which can be operated on String url such as...

Example :- 
          try {
          
            URL downloadurl=new URL(downloadString);
            
             downloadurl.getAuthority()... getDefault..()... etc etc...
             
            }catch (MalformedURLException e) {
            
            Log.i(TAG, "MalformedURLException:: " + e);
            
        }
   
And we surround the above URL statement with try-catch block because it may throw MalformedURLException in case of invalid urls

Example :- 

http://netdna.webdesignerdepot.com/uploads/2008/11/sample-graphic.jpg (valid)

http;//netdna.webdesignerdepot.com/uploads/2008/11/sample-graphic.jpg (invalid because there is ; instead of :)
