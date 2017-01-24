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

# arcs that respect triangular inequality
set A := setof{i in S, j in S: i<>j and l[i,j]+l[j,school] <= alpha*l[i,school]} (i,j) diff setof{i in S}(0,i);


var x{i in S, j in S} >=0, binary;
var y{i in S}, binary;
