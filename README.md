# batch

BATCH (fBk keyphrAses sofT Clustering algoritHm) is an open source library for clustering keyphrases (expressions which help understand and summarize the content of documents) in text documents. It uses an algorithm based on graph connectivity for Cluster analysis, by first representing the similarity among keyphrases in a similarity graph, and then finding all the connected subgraphs (groups of keyphrases that are connected to one another, but that have no connections to keyphrases outside the group) as clusters. The algorithm does not make any prior assumptions on the number of the clusters.

BATCH has been designed to meet the following requirements:

- Method: 
  - Soft agglomerative clustering: a keyphrase can belong to more than one cluster.
  - Constrained clustering: use must-link constraints (i.e., keyphrases compatibility relations) to specify that two Keyphrases in a must-link relation have to be associated with the same cluster.
  - Incremental clustering: new keyphrases added to the data collection without having to perform a full re-clustering
- Efficiency: parallelized to exploit multi-core processors. 
- Portability: written in Java to be portable across different platforms.
- Simplicity:  implemented as a maven project to make it easy to install, configure and use. A Command Line Interface (CLI) is provided for convenience of experiments.

keyphrases compatibility relations (or criteria) are used to specify that two keyphrases has to be assigned into one cluster. The current implemented relations are:
- PrepositionalVariant (id=1): kj and ki are variants if they have the same tokens except for one preposition or article preposition contractions. E.g., 'sacchetti con caramelle' and 'sacchetti di caramelle'
- Abbreviation (id=2): kj and ki must have the same number of tokens in the same order, and the number of tokens must be more than 1, one token in kj (and not more than one) can be the abbreviation of one token in ki. E.g., 'banca generali' and 'b. generali'
- Acronym (id=3): kj and ki are variants if kj consists of one token of n>1 letters and ki consists of n tokens; the initials of the n tokens of ki are the letters composing the single token of kj in the same order. To be written.
- Singular/Plural (id=4): check if two variants have the same lemma and one or more (at least one) is the plural of the other one. E.g., 'attività professionali' and 'attività professionale'
- ModifierSwap (id=7): check if all tokens are the same in different order and the head is the same (no permutation of the head). E.g., 'corte di giustizia europea' and 'corte europea di giustizia' 
- Entailment (id=9): check if two variants have the same semantic head and one has just one token less. E.g., 'rinnovo del contratto nazionale' and 'rinnovo del contratto'
- Synonym (id=11) kj and ki are variants if they consist of more than one token and one of the tokens in kj is a synonym of ki in MultiWordNet. E.g., 'incidente sul lavoro' and 'infortunio sul lavoro'
- Article (id=12) kj and ki are variants if they have the same number of tokens but one contains a simple preposition and the other one contains an article/preposition contraction where the lemma of the simple preposition is the same as the part of the lemma of the contraction. E.g., 'delibera di giunta' and 'delibera della giunta'


## System Architecture

The algorithm consists of 3 main steps:

- reading the keyphrases and filtering out the duplicates
- comparing the keyphrases each other and building the graph
- traversing the graph by Breadth First Search (BFS) and printing all the connected subgraphs (clusters)

During the first step the keyphrases produced by KD are read and the duplicates removed. Two keyphrases are the same keyphrase if they have both the same form (case sensitive string match) and Part-of-Speech. Then, in the second step, the unique keyphrases are compared each other by applying the implemented criteria (e.g., abbreviation). When a criterion has been successfully applied, then a link between the 2 keyphrases is created. The result of this step is an adjacency list containing all the possible links between the keyphrases. Finally, in the third and last step, all the connected sub-graphs are discovered by Breadth First Search (BFS) and the produced clusters saved into files that are in .xml format.


## Getting started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

- Java 1.8 or later
- Apache Maven (http://maven.apache.org) (optional)

### How to get the code

The jar file containing all the Java code can be download from this address: 

https://github.com/rzanoli/batch/releases/download/v0.1-alpha/batch-0.0.1-SNAPSHOT-jar-with-dependencies.jar


### Dataset format

Files in input contain the documents annotated by TextPro (http://textpro.fbk.eu/) and the keyphrases produced by KD (https://dh.fbk.eu/technologies/kd) in IOB2 format, e.g.,
```
Sulla     ES  su/det    O
salute    SS  salute    B-KEY
del       ES  di/det    I-KEY
pianeta   SS  pianeta   I-KEY
stiamo    VI  stare     O
perdendo  VG  perdere   O
la        RS  det       O
battaglia SS  battaglia B-KEY
.         XPS full_stop O
```

The output consists of xml files containing the produced clusters, e.g.,
```
<KEC_graph id="88766" node_count="2">
     <node id="88766" root="true">
          <text>kit di scrittura gratuito</text>
          <ids>9767800.txt.txp.iob_10</ids>
     </node>
     <node id="140151" root="false">
          <text>kit gratuito di scrittura</text>
          <ids>9891493.txt.txp.iob_14 9917114.txt.txp.iob_10</ids>
     </node>
     <edge relation_role="7" source="88766" target="140151"/>
</KEC_graph>
```

### Installation

Save the BATCH jar file that you have just downaloded into your working directory.

### Running the code

From your working directory, run the following command:

```>java -cp batch-0.0.1-SNAPSHOT-jar-with-dependencies.jar eu.fbk.hlt.nlp.cluster.Runner  -i dirIn -o dirOut -g graphOut [-a graphIn] [-p processes]```

Where: 
- dirIn: the directory containing the keyphrases to cluster
- dirOut: the directory containing the produced clusters in xml format
- graphOut the directory containing the produced adjacency list and keyphrases index
- graphIn: the adjacency list and the keyphrases index of a previuous clustering phase to perform incremental clustering
- processes: the number of processes to perform clustering (default 1)

## Authors

- Alberto Lavelli
- Bernardo Magnini
- Manuela Speranza
- Roberto Zanoli

## License

