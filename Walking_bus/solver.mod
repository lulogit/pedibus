param n;			# number of stops
set S ordered := 0 .. n;	# pedibus stops

# school node
param school := 0;
param alpha;

param coordX{S};
param coordY{S};

# arc danger
param d{S,S};

# arc length
param l{i in S, j in S} := sqrt((coordX[i]-coordX[j])^2+(coordY[i]-coordY[j])^2);

# radius
param r{i in S} := l[i,school];

# arcs that respect triangular inequality
set A := setof{i in S, j in S: i<>j and l[i,j]+l[j,school] <= alpha*l[i,school]} (i,j) diff setof{i in S}(0,i);

# feasible path for each node
set P{k in S} := 

# max danger (the sum of all dangers): to weight the number of leaves in objective function
param maxd := sum{i in S,j in S: (i,j) in A} d[i,j];


# mean radius
param rmean := (sum{i in S} r[i]) / n;

# outer stops
set O := setof{i in S: r[i]>=rmean+0.0001} i;

var x{i in S, j in S} >=0, binary;
var y{i in S}, binary;

minimize NumberOfChapelier:
	maxd * (sum{i in S} y[i]) + (sum{i in S,j in S: (i,j) in A} d[i,j]*x[i,j]);

subject to MinLeavesConstraint{i in S diff {school}}:
	1-sum{j in S: (j,i) in A} x[j,i]  <= 2*y[i];

subject to c1{i in S diff {school}}:
	sum{j in S: (i,j) in A} x[i,j] = 1;
subject to c2{i in S diff {school}}:
	sum{j in S: (j,i) in A} x[j,i] <= 1;
subject to c3{i in S, j in S: not (i,j) in A}:
	x[i,j] = 0;


