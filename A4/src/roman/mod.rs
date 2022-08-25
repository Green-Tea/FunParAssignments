use std::collections::HashMap;

#[allow(dead_code)]
pub fn to_roman(n: u16) -> String {
    let symbols = vec![
        "M", "CM", "DC", "D", "CD", "C", "XC", "LX", "L", "XL", "X", "IX", "VI", "V", "IV", "I",
    ];
    let decimals = vec![
        10000, 900, 600, 500, 400, 100, 90, 60, 50, 40, 10, 9, 6, 5, 4, 1,
    ];
    let mut k = n;
    let mut i = 0;
    let mut res = String::new();

    while k > 0 {
        if k >= decimals[i] {
            k -= decimals[i];
            res.push_str(symbols[i]);
        } else {
            i += 1
        }
    }

    res
}

#[allow(dead_code)]
pub fn parse_roman(roman_number: &str) -> u16 {
    let symbols = HashMap::from([
        ("M", 1000),
        ("D", 500),
        ("C", 100),
        ("L", 50),
        ("X", 10),
        ("V", 5),
        ("I", 1),
    ]);

    let mut n = 0;
    let mut res = 0;

    while n < symbols.len() {
        let condition = symbols.get(&*roman_number.chars().nth(n).unwrap().to_string())
            < symbols.get(&*roman_number.chars().nth(n + 1).unwrap().to_string());

        if condition && n != roman_number.len() - 1 {
            res += symbols
                .get(&*roman_number.chars().nth(n + 1).unwrap().to_string())
                .unwrap()
                - symbols
                    .get(&*roman_number.chars().nth(n).unwrap().to_string())
                    .unwrap();
            n += 2;
        } else {
            res += symbols
                .get(&*roman_number.chars().nth(n).unwrap().to_string())
                .unwrap();
            n += 1;
        }
    }

    res
}

#[cfg(test)]
mod tests {
    use super::{parse_roman, to_roman};

    #[test]
    fn basic_digits() {
        assert_eq!("I", to_roman(1));
        assert_eq!("V", to_roman(5));
        assert_eq!("X", to_roman(10));
        assert_eq!("L", to_roman(50));
        assert_eq!("C", to_roman(100));
    }

    #[test]
    fn basic_mixture() {
        assert_eq!("II", to_roman(2));
        assert_eq!("IV", to_roman(4));
        assert_eq!("IX", to_roman(9));
        assert_eq!("XII", to_roman(12));
        assert_eq!("XIV", to_roman(14));
        assert_eq!("MCMLIV", to_roman(1954));
    }

    #[test]
    fn basic_parsing() {
        assert_eq!(3, parse_roman("III"));
        assert_eq!(4, parse_roman("IV"));
        assert_eq!(8, parse_roman("VIII"));
        assert_eq!(19, parse_roman("XIX"));
    }
}
