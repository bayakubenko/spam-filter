# spam-filter

This small program was modeled after Paul Grahms write up to create an automated process to filter out spam messages without leveraging any AI technologies or techniques. The application reads a corpus of spam and non spam messages where each line identifies the message body as `spam` or `ham`.

The write up can be found in the following link http://www.paulgraham.com/spam.html. The program is developed in JAVA 8 and to maintain simplicity I only leveraged JRE native classes. The application driver program is `Application.java` and for demonstration purposes there are two outputs. The first output is data visualization of parsed input file as an example of data analytics. Data visualization is powerful and it is key in understanding the data when developing data driven solutions, even more so when developing distributed solutions. The second output illustrates comparison of the spam detector accuracy. For demonstration purposes I perform two tests to determine the accuracy of the spam filter with and with out uninteresting words. 

The set of uninteresting words is the following: "to", "you", "we", "I", "i", "a", "and", "is", "the", "in", "u", "my", "me", "of", "for", "it", "your", "i'm", "on", "for", "ur", "that", "have", "are", "or", "at", "not", "be"

The goal here is to understand if the spam detectors accuracy is improved if the uninteresting words are filtered out during the parsing and test stages. It is understood that this list is not fully complete but simply intended for demonstration purposes.

### Data Analysis Output:
```
Processed input file in Total time (hh:mm:ss:SSS): [0:0:0:893]
************* Unfiltered Metrics *************
Top line metrics ----
Total SMS occurances: 5574
Total Ham occurances: 4827
Total Spam occurances: 747
Percentage of Ham occurances: 86.6
Percentage of Spam occurances: 13.4

Ham corpus metrics ----
Total Ham token occurances: 69046
Average token occurances per Ham corpus: 14
Top 10 tokens for Ham corpus
i: 2181
you: 1669
to: 1552
the: 1124
a: 1058
u: 881
and: 846
in: 790
my: 745
is: 717

Spam corpus metrics ----
Total Spam token occurances: 17862
Average token occurances per Spam corpus: 23
Top 10 tokens for Spam corpus
digits: 873
to: 685
a: 375
call: 342
phonenumber: 315
your: 263
you: 252
currency: 249
the: 204
for: 201

********* Filtered out uninteresting tokens Metrics *********

Ham corpus metrics ----
Total Ham token occurances: 51131
Average token occurances per Ham corpus: 10
Top 10 tokens for Ham corpus
digits: 658
but: 415
so: 399
can: 356
if: 350
do: 341
will: 336
get: 293
just: 287
&lt;#&gt;: 276

Spam corpus metrics ----
Total Spam token occurances: 14365
Average token occurances per Spam corpus: 19
Top 10 tokens for Spam corpus
digits: 873
call: 342
phonenumber: 315
currency: 249
free: 180
txt: 136
from: 127
text: 112
mobile: 109
with: 108
```
### Spam Detector Output:
```
Processed input file in Total time (hh:mm:ss:SSS): [0:0:0:860]
Test accuracy of unfiltered dataset: 100.0
Test accuracy of filtered dataset: 98.0
```
### Results:
Spam detector test is performed on 50 random lines selected from the input file. The accuracy is computed by comparing the first token of the message line to the output of the interfacing method in `SpamDetector.java`. In conclusion, the unfiltered data set performed slightly better after executing the process multiple times. 