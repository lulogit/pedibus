java -jar pedibus.jar "../test_data/pedibus_$1.dat"
python ../solver/pedibus_checker.py "../test_data/pedibus_$1.dat" "../test_data/pedibus_$1.sol"
