# socialmediabot
Bot that posts to social media regularly
Populate the app.properties with your personal access code/token for imgur & buffer
Setup tensor flow with inception at the directory you specify in the properties file

Details at :http://blog.wikibackpacker.com/2016/07/social-media-bot-with-deep-learning.html
Get imgur album and list the images via imgur api
 select a random image from the album
 if image has title & description set then use that for posting text
 If there is no title & description then
        4.a) download the image
        4.b) pass the image to TensorFlow inception to guess the contents of the image
        4.c) create hashtags out of the top three Inception guesses
 Subscribe to Buffer (shout out to Buffer.com for such an awesome app)
Create social profiles on Buffer where I wanted the post to appear (I ended up creating twitter, facebook, linkedin, Google+ & Instagram)
For each social profile get the image link, the post text (which could be the image title on imgur or guessed by inception) and post it to Buffer using Buffer api.
Setup this program on crontab
 Make sure the amount of times the crontab runs is less then or equal to the amount of posting times on buffer (else the queue buffer keeps filling up on Buffer)

