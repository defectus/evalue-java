package tk.deftech.evalue

import spock.lang.Specification

class MainSpecs extends Specification {

    def "input line can be parsed"() {
        when:
            Main.PaymentEntry entry = new Main().parseLine("USD 100")
        then:
            entry.amount == 100 as BigDecimal
            entry.currencyCode == 'USD'
    }

    def "output can be generated"() {
        given:
            Map<String, BigDecimal> values = [USD: 100 as BigDecimal, CZK: 100 as BigDecimal]
        when:
            String output = new Main().generateOutput(values)
        then:
            output == 'USD 100\nCZK 100\n'
    }

    def "new payment can be added"() {
        given:
            Main main = new Main()
        when:
            main.addPayment(new Main.PaymentEntry('USD', 100 as BigDecimal))
        then:
            main.values == [USD: 100 as BigDecimal]
    }

    def "payment can be added to existing payment"() {
        given:
            Main main = new Main()
            main.values = [USD: 100 as BigDecimal]
        when:
            main.addPayment(new Main.PaymentEntry('USD', 100 as BigDecimal))
        then:
            main.values == [USD: 200 as BigDecimal]
    }

    def "payment that equals to 0 drops of the list"() {
        given:
            Main main = new Main()
            main.values = [USD: 100 as BigDecimal]
        when:
            main.addPayment(new Main.PaymentEntry('USD', -100 as BigDecimal))
        then:
            main.values == [:]

    }
}
