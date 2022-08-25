use rayon::iter::ParallelIterator;
use rayon::iter::{IntoParallelIterator, ParallelBridge};

#[allow(dead_code)]
pub fn par_is_prime(n: u64) -> bool {
    if n <= 1 {
        false
    } else {
        let a = (n as f64).sqrt() as u64;
        let nums: Vec<u64> = (2..=a).collect();

        nums.iter()
            .take_while(|x| *x <= &a)
            .par_bridge()
            .all(|x| n % x != 0)
    }
}

#[allow(dead_code)]
pub fn par_count_primes(n: u32) -> usize {
    let nums: Vec<u32> = (2..=n).collect();

    nums.into_par_iter()
        .filter(|x| par_is_prime(*x as u64) == true)
        .count()
}

#[cfg(test)]
mod tests {
    use crate::numprimes::{par_count_primes, par_is_prime};

    #[test]
    fn basic_is_prime() {
        assert_eq!(false, par_is_prime(0));
        assert_eq!(false, par_is_prime(1));
        assert_eq!(true, par_is_prime(2));
        assert_eq!(true, par_is_prime(3));
        assert_eq!(false, par_is_prime(6));
        assert_eq!(false, par_is_prime(25));
        assert_eq!(true, par_is_prime(41));
        assert_eq!(false, par_is_prime(100_000));
        assert_eq!(true, par_is_prime(21803));
        assert_eq!(true, par_is_prime(21799));
        assert_eq!(false, par_is_prime(22222));
    }
    #[test]
    fn basic_count_primes() {
        assert_eq!(25, par_count_primes(100));
        assert_eq!(78498, par_count_primes(1_000_000));
        assert_eq!(1, par_count_primes(2));
        assert_eq!(2, par_count_primes(3));
        assert_eq!(2, par_count_primes(4));
        assert_eq!(0, par_count_primes(1));
    }
}
