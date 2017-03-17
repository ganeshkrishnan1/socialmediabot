   
   # socialmediabot

## Synopsis

Bot that posts to social media regularly by picking up random images from your imgur album & analyzing it's contents

## Code 

Uses groovy, Google TensorFlow Inception

## Motivation

After my last debacle at attempting to find a social media person who could make few posts on my twitter, facebook & instagram daily I decided to write my own Social Media Manager Bot.

## Installation

* Populate the app.properties with your personal access code/token for imgur & buffer
* Setup tensor flow with inception at the directory you specify in the properties file
* Run the app by groovy 

```
 groovy src/main/groovy/BufferSocialMedia.groovy
```

## Tests

Checkout the posting on our social media accounts to see it in action

## License

MIT

**Free Software, Hell Yeah!**

## Sample Outputs

Our automated postings are on

[Facebook](http://www.facebook.com/wikibackpacker)

[Twitter](http://www.twitter.com/wikibackpacker)

[Google+](https://plus.google.com/b/104257227793890800020/104257227793890800020)

[Instagram](http://www.instagram.com/wikibackpacker/)

[linkedin](https://www.linkedin.com/company/7957818)




Detailed Blog  at :[http://blog.wikinomad.com/2016/07/social-media-bot-with-deep-learning.html](http://blog.wikibackpacker.com/2016/07/social-media-bot-with-deep-learning.html)

A breakdown of what this bot does:

 * Get imgur album and list the images via imgur api
 * select a random image from the album
 * if image has title & description set then use that for posting text
 * If there is no title & description then
 
        a) download the image
        b) pass the image to TensorFlow inception to guess the contents of the image
        c) create hashtags out of the top three Inception guesses
 * Subscribe to Buffer (shout out to Buffer.com for such an awesome app)
 * Create social profiles on Buffer where I wanted the post to appear (I ended up creating twitter, facebook, linkedin, Google+ & Instagram)
 * For each social profile get the image link, the post text (which could be the image title on imgur or guessed by inception) and post it to Buffer using Buffer api.
 * Setup this program on crontab
 * Make sure the amount of times the crontab runs is less than or equal to the amount of posting times on buffer (else the queue buffer keeps filling up on Buffer)

