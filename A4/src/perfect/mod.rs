use crate::perfect::Classification::{Deficient, Excessive, Perfect};
use std::{cmp::Ordering, ops::Range};

#[allow(dead_code)]
#[derive(Debug, Eq, PartialEq)] // make this enum type support equality test (i.e., ==)
pub enum Classification {
    Perfect,
    Deficient,
    Excessive,
}

#[allow(dead_code)]
pub fn classify_perfect(n: u64) -> Classification {
    return if divisor_sum(n) > n {
        Excessive
    } else if divisor_sum(n) < n {
        Deficient
    } else {
        Perfect
    };
}

#[allow(dead_code)]
pub fn select_perfect(range: Range<u64>, kind: Classification) -> Vec<u64> {
    range
        .into_iter()
        .filter(|&x| classify_perfect(x) == kind)
        .collect::<Vec<u64>>()
}

fn divisor_sum(n: u64) -> u64 {
    (1..n)
        .into_iter()
        .filter(|&x| n % x == 0)
        .collect::<Vec<u64>>()
        .iter()
        .sum()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn basic_classify() {
        use Classification::*;
        assert_eq!(classify_perfect(1), Deficient);
        assert_eq!(classify_perfect(6), Perfect);
        assert_eq!(classify_perfect(12), Excessive);
        assert_eq!(classify_perfect(28), Perfect);
        assert_eq!(classify_perfect(100), Excessive);
        assert_eq!(classify_perfect(2), Deficient);
        assert_eq!(classify_perfect(9999), Deficient);
    }

    #[test]
    fn basic_select() {
        use Classification::*;
        assert_eq!(select_perfect(1..10_000, Perfect), vec![6, 28, 496, 8128]);
        assert_eq!(
            select_perfect(1..50, Excessive),
            vec![12, 18, 20, 24, 30, 36, 40, 42, 48]
        );
        assert_eq!(
            select_perfect(1..11, Deficient),
            vec![1, 2, 3, 4, 5, 7, 8, 9, 10]
        );
        assert_eq!(select_perfect(69..420, Perfect), vec![]);
        assert_eq!(
            select_perfect(10_000..10_010, Excessive),
            vec![10000, 10002, 10008]
        );
    }
}
