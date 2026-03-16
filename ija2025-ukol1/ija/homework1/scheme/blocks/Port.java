/*
 * IJA (Seminář Java): 2025/26 Ukol 1
 * Author:  Radek Kočí, VUT FIT
 * Created: 02/2026
 */
package ija.homework1.scheme.blocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraktní třída reprezentující port bloku.
 * Každý port má název a vlastníka (blok) a uchovává informaci o poslední hodnotě.
 * Implicitní hodnota (pokud ještě nebyla žádná nastavena) je 0.
 * Vstupní port bloku je možné napojit na výstupní port jiného bloku, příp. jiných bloků.
 * Port pracuje s jedinou hodnotou typu double.
 */
public abstract class Port {

    /** Název portu (např. "a", "b", "out") */
    private String name;

    /** Blok, kterému port patří */
    private Block owner;

    protected Port(String name, Block owner) {
        if (name == null) throw new IllegalArgumentException("Port name cannot be null.");
        if (owner == null) throw new IllegalArgumentException("Port owner cannot be null.");
        this.name = name;
        this.owner = owner;
    }

    /**
     * Vstupní port bloku.
     * Přijímá hodnotu z výstupního portu jiného bloku, po každém přijetí vyvolá přepočet svého bloku ({@link Block#calculate()}).
     */
    public static class InputPort extends Port {

        private Double value = 0.0; // poslední přijatá hodnota
        private OutputPort source;

        public InputPort(String name, Block owner) { super(name, owner); }

        /**
         * Připojí vstupní port na výstupní blok zadaného bloku.
         * @param block Blok, na jehož výstupní port se připojí tento vstupní port.
         * @throws IllegalArgumentException pokud je block == null.
         */
        public void connect(Block block) {
            if (block == null) throw new IllegalArgumentException("Block cannot be null.");
            this.source = block.getOutputPort();
            source.addConnection(this);
        }

        /**
         * Vrátí aktuální hodnotu výstupního portu. 
         * @return Hodnota portu.
         */
        public double getValue() {
            return value;
        }

        /**
         * Zkontroluje, zda je tento vstupní port připojen k nějakému výstupnímu portu.
         * @return true pokud je připojen, false jinak.
         */
        public Boolean isConnected() {
            return source != null;
        }

        /**
         * Aktualizuje hodnotu tohoto vstupního portu na základě hodnoty z připojeného výstupního portu.
         * A následně vynutí přepočet bloku.
         */
        protected void propagateSource() {
            this.value = source.getValue();
            super.owner.calculate(); // vynucení přepočtu bloku při změně vstupu
        }
    }

    /**
     * Výstupní port bloku.
     * Při každém přepočtu (změně výsledku) bloku se nová hodnota uchová ve výstupním portu a propaguje se do připojených 
     * vstupních portů jiných bloků.
     */
    public static class OutputPort extends Port {

        private double value;
        private List<InputPort> connections = new ArrayList<>();

        public OutputPort(String name, Block owner) { super(name, owner); }

        /**
         * Vrátí aktuální hodnotu výstupního portu. 
         * @return Hodnota portu.
         */
        public double getValue() {
            return value;
        }

        /**
         * Přidá port který je připojen a čte tento výstupní port.
         * @param inputPort
         */
        public void addConnection(InputPort inputPort) {
            connections.add(inputPort);
        }

        public void setValue(double value) {
            this.value = value;
            // Propagace nové hodnoty do všech připojených vstupních portů
            for (InputPort input : connections) {
                input.propagateSource(); // aktualizace hodnoty v připojeném vstupním portu
            }
        }
    }
}
