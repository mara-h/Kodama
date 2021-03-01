# Kodama
Team members:
Mara Husar, Oana Vasilescu – MANA

Specifications KODAMA:
The application is designed to help you identify plants. If you come across a plant and you want to know more about by simply taking a picture of it, you’ll find everything you need to know.  
With the help of artificial intelligence (image recognition), the app will display the plant’s name.
The appplication achieves good identification accuracy and provides users a useful system for plant identification.
       			   			 
Application appearance: 
-	When opening the app, the user will be met by a simple virtual camera. There will also be a button for “upload”, if the picture already exists in the phone’s library, and a history button that will show you the names of previous examined plants.
-	After analyzing the picture, the name/species of the plant will be displayed. Also there will be a “details” button that would open a google search for that plant

Implementation:

-	The app wil have to capture a photo by delegating the work to the default camera app used by the phone
-	Must find an open source image dataset which will be used to train the AI (ex: imagenet, mscoco.org , Yahoo Webscope Program, Open Images, quantitive-plant.org, https://www.imageclef.org/2013/plant, trefle.io(i think this is not an image based, but rather gives info about some plants), plants.usda.gov, wildflower.org)
-	Alternatively, we can use http://deep.ifca.es/plants/  and query this webpage for predictions through an API
-	If we implement the machine learning ourselves, we can use neuroph, deepnetts, Deeplearning4j, AlgART;


