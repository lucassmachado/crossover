<img src="https://www.crossover.com/wp-content/uploads/2015/09/95c6a29b.logo2_.png" width="400" />

# Crossover Java Trial Project - Weather

## Project Background

The Airport Weather Service (AWS) is a REST application for collecting and redistributing meteorological data for a
handful of airports. The service provides two distinct interfaces. One is a query interface, used by dozens of client
systems to retrieve information such mean temperature and max wind speed. The other is a collector interface used by
airports to update meteorological data stored in AWS. You'll find more detail about these interfaces in
WeatherCollectorEndpoint.java and WeatherQueryEndpoint.java. To see the service in action, you can run the
included run-ws.sh which launches the server and demonstrates a simple client hitting the REST endpoint.

Over time, the number of airports and clients of the service has grown. Today, the application finds itself filling a
role much larger than it was initially designed for - resulting in poor reliability and slow performance. Your task is
to apply best practices to the code and fix design defects, while keeping in mind that since this is a live application,
you must be careful not to change external interfaces.

### REST API

The application currently supports the following REST APIs, you should not change the parameters or response format, but may change the implementation.
* GET /collect/ping
* POST /collect/weather/{iata}/{pointType}
* GET /query/ping
* GET /query/weather/{iata}/{radius}

Unimplemented REST APIs
* POST /collect/airport/{iata}/{lat}/{long}
* DELETE /collect/airport/{iata}

## Assignment Objectives
Your goal, as an experienced software engineer, is to apply java development best practices and convention to the AWS
code. You should modify the code to follow the patterns in the java world, restructure the code so that it's easier to
 work with, but keep in mind that the result needs to retain the REST API spec established in the first version.

### Part 1 - Take ownership and update to best practices
The code is now *your* code - change it to meet professional coding standards. You should take this opportunity to fix
obvious logic and concurrency bugs. You may update data structures as necessary, but changing the external contract will
 result in an automatic failure on the assignment.

### Part 2 - Add a new feature
Now that the code is cleaned up, it's time to add a feature. The feature to implement, which you may have noticed in
your code review, is that airports are hard coded. You should implement the ```[POST|DELETE] /collect/airport```
endpoints. You should also create an ```AirportLoader``` which will read airports.dat and call your endpoint.
The application will need to support at least 1000 unique airports.

A sample airports.dat is included with the trial, but a more complete airport.dat will be used to evaluate your work.
The airports.dat is a comma separated file with the following headers:

Header   	| Description
------------|------------
City		| Main city served by airport. May be spelled differently from name.
Country		| Country or territory where airport is located.
IATA/FAA 	| 3-letter FAA code or IATA code (blank or "" if not assigned)
ICAO		| 4-letter ICAO code (blank or "" if not assigned)
Latitude 	| Decimal degrees, up to 6 significant digits. Negative is South, positive is North.
Longitude	| Decimal degrees, up to 6 significant digits. Negative is West, positive is East.
Altitude	| In feet
Timezone	| Hours offset from UTC. Fractional hours are expressed as decimals. (e.g. India is 5.5)
DST			| One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown) 

## Solution Requirements
1.  You should not change any public REST interfaces (this is your final warning)
2.  We will build and test your submission using the docker image maven:3.3-jdk-8
3.  You should follow the packaging and submission guidelines below. Automated grading can handle minor differences in submission structure, but there is a scoring penalty for not following submission guidelines.

## Scoring
1. Correctness (40%) 
  * Any submission that does not build or fails the included unit tests will not be graded.

  * There are some obvious bugs in the existing code, successful candidates find and fix these bugs.

  * The autograder will apply production like workloads against your submission. You should use the narrative above and
    your own experience to create a submission suitable for such use.

2. Code Quality (60%)

  Code is read many more times than it is written and when considering the effectiveness of a developer,
  we place considerable weight on how easy that developer's code is to read and maintain. As a result a
  significant portion of your score is derived from the overall craftmanship of your code. We consider the
  following:

  * Code Complexity - It's often easier to write complex code with branches and special conditions, but such code
    is generally harder to read, understand and maintain. Code with fewer branches using, higher locality of data
    and clear encapsulation will score better.

  * Code Formatting - The java compiler, for the most part, ignores the formatting of your code. However, other
    developers typically find it easier to read code that follows a consistent style. Java's
    style guide has been around almost as long as the language itself and many experienced developers find reading
    code formated using the Oracle or Google styles to be easier to read and maintain. We find that experienced
    developers write code in this style without even thinking about it.

  * Standards and Best Practice - There are some best practices when developing code that greatly improve its long
    term maintainability. These best practices help to avoid common mistakes and bugs - for example, closing
    input streams, only calling non-virtual methods from constructors and using constants. While it's not strictly
    incorrect to not follow these principles, strong developers follow these standards intuitively and our
    experience has shown that such code is easier to maintain and contains fewer corner cases.

  * Testing - Creating unit testable code along with positive and negative test cases helps the author ensure
    that important scenarios work correctly. It also lets future maintainers to confidently apply modifications.

## Delivery / What to submit

### Packaging Your Code
Once you're done, run the provided package.sh, which will create add pom.xml, src, package.sh and run-ws.sh
to a submittable zip file. You'll need to rename the zip before submission - use the naming convention
\{Position\}-AWS-{last_name}\_{first_name}.zip where position is acronym for the position you are applying to, for
example a Senior Chief Architect (SCA) candidate might use SCA_AWS_Gosling_James.zip.

### Verifying your deliverable
Although optional, we suggest extracting your final deliverable into a new directory to make sure it builds and
tests pass.

Good luck!
