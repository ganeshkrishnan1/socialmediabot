/**
 * Created by spyder on 12/10/15.
 */

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC

import java.util.Random  

@Grab(group='org.codehaus.groovy.modules.http-builder', 
      module='http-builder', version='0.7.1' )
//@GrabConfig( systemClassLoader=true )
class BufferSocialMedia {

static def clientid="x";
static def accesstoken="x";
static def clientsecret ="x";
static def imgurclient ="x"
static def imgurcode="x"
static def tensorflowdir="/media/spyder/BitEast/project/tensorflow/tensorflow/"
static def imguralbum="bPt5C"
private static Random rand = new Random() 

   public static void main(String[] args) {
  println "Starting the Buffer Social media posting ..."
  /*
  We need an app.properties file with name of properties matching the variables above
  */
  Properties properties = new Properties()
File propertiesFile = new File('app.properties')
propertiesFile.withInputStream {
 
    properties.load(it)
}
println "loaded properties $properties";
clientid=properties.clientid;
 accesstoken=properties.accesstoken;
 clientsecret =properties.clientsecret;
 imgurclient =properties.imgurclient;
imgurcode=properties.imgurcode;
 tensorflowdir=properties.tensorflowdir;
 imguralbum=properties.imguralbum;



 def imglink ="http://i.imgur.com/LPtBnIM.jpg";
 String imgtitle= "WikiBackPacker: Live, Love, Travel";
 String imgdescription= "WikiBackPacker: Live, Love, Travel";
 String posttext="";
 def deepprocess=true;

println "*" * 100;
 println "Fetching all images from imgur ..."
 
def imgurhttp = new HTTPBuilder( 'https://api.imgur.com/3/album/bPt5C' )
 imgurhttp.request(GET,JSON) { req ->
  uri.path = '/3/album/bPt5C'
headers.'Authorization' = ' Client-ID ' +imgurclient
 headers.Accept = 'application/json'

  response.success = { resp, jsonresp ->
println "Success imgur response ";
String[] images = jsonresp.data.images;
int max= images.length;
println " We have total ${images.length} images"
int getImage= rand.nextInt(max+1);
//getImage=1; //debug
def imagetouse = images[getImage];
imglink= jsonresp.data.images[getImage].link;
if(jsonresp.data.images[getImage].title)
{
imgtitle=jsonresp.data.images[getImage].title;
deepprocess=false;
}
if(jsonresp.data.images[getImage].description)
{
  imgdescription=jsonresp.data.images[getImage].description;
  posttext= imgdescription;
  deepprocess=false;
}

println " We will use  number ${getImage} : ${imagetouse} with link ${imglink} Text : ${imgtitle}/${imgdescription}" 
  }

  response.failure={
     resp, jsonresp ->
     println "imgur got FAIL response $jsonresp";
     System.exit(1);
  }

 }


 if(deepprocess){
   println "*" * 100;
println "deep neural processing for image ${imglink}"
println "Downloading images ${imglink} .. "
String downfile=downloadimige("tmpfile",imglink)

def command = new String[3]
command[0] = "sh"
command[1] = "-c"
command[2] = "./bazel-bin/tensorflow/examples/label_image/label_image  --image="+ downfile;
def processBuilder=new ProcessBuilder(command)
processBuilder.redirectErrorStream(true)
processBuilder.directory(new File(tensorflowdir.toString())) 
def process = processBuilder.start();
int tags=0;
  imgtitle = " ";
   imgdescription = " ";
process.inputStream.eachLine {

  if(it.startsWith("I tensorflow/examples/label_image/main.cc:210]"))
  {
 int braceindex = it.indexOf("(")
 def val = it.substring("I tensorflow/examples/label_image/main.cc:210] ".length(), braceindex);
 println "Label for image is $val"

 if(tags++ <4)
 {
   imgtitle += " ${val} "
   imgdescription += " ${val} "
 posttext += "#${val} "
 }
  }
  }

}

println "Image ${imglink} has decoded to ${imgtitle}/${imgdescription}"

println "*" * 100;

println "Starting buffer postings ..."
 def http = new HTTPBuilder( 'https://api.bufferapp.com/1/profiles.json' )

 http.request(GET,JSON) { req ->
  uri.path = '/1/profiles.json?access_token='+accesstoken;  // overrides any path in the default URL
  headers.'User-Agent' = 'Chrome/5.0'

  response.success = { resp, jsonresp ->
    println "My response handler got response: ${resp.statusLine}"
    println "Response length: ${resp.headers.'Content-Length'}"

jsonresp.each{socialprofile ->
println "Social profile:${socialprofile.formatted_service} is ${socialprofile}"

println "Posting to ${socialprofile.formatted_service}..."
def socialhttp = new HTTPBuilder( 'https://api.bufferapp.com/1/updates/create.json' )

socialhttp.request( POST ) {
    uri.path ='/1/updates/create.json'
    requestContentType = URLENC
    body = ["profile_ids[]": "${socialprofile.id}", text: posttext,
    "media[photo]": imglink,"access_token":accesstoken,"media[description]": "${imgdescription.toString()}",
    "media[xlink]": imglink,"media[title]": "${imgtitle.toString()}","shorten":"false",
    "attachment":"true" ] 
println "Posting body $body";
    response.success = { postresponse ->
        println "POST response status: ${postresponse.statusLine}"  
    }

     response.failure={
     postresponse, failresp ->
     println "BUFER POST got FAIL response $failresp";
 
  }
}

println "=" * 50;
}

  }

}



     
  



   }



 public static String downloadimige( String tmpfilename,def address) {
  new File("/tmp/${tmpfilename}.jpg").withOutputStream { out ->
      new URL(address).withInputStream { from ->  out << from; }
  }
println "download of $address to /tmp/${tmpfilename}.jpg complete"
  return "/tmp/${tmpfilename}.jpg";
}


          
}


