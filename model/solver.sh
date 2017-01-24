sed "s|{DAT}|$1|" solver.run | ./ampl | grep -P "\([0-9]+\,[0-9]+\)" -o | sed 's/(//g; s/)//g; s/,/ /g;' > `echo "$1" | sed "s|.dat$|.sol|"`
python pedibus_checker.py "$1" `echo "$1" | sed "s|.dat$|.sol|"`
