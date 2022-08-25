use chashmap::CHashMap;
use rayon::iter::{IntoParallelRefIterator, ParallelIterator};
use std::collections::HashMap;

pub fn par_char_freq(chars: &[u8]) -> HashMap<u8, u32> {
    chars
        .par_iter()
        .map(|x| {
            let mut map = HashMap::<u8, u32>::new();
            *map.entry(*x).or_insert(0) += 1;

            map
        })
        .reduce(
            || HashMap::new(),
            |mut x, y| {
                for (i, k) in y {
                    *x.entry(i).or_insert(0) += k;
                }
                x
            },
        )
}

#[cfg(test)]
mod tests {
    use crate::charfreq::par_char_freq;
    use std::io::Read;

    #[test]
    fn basic_tests() {
        println!("{:?}", par_char_freq("lorenzo".as_bytes()));
        println!("{:?}", par_char_freq("horse".as_bytes()));
        println!("{:?}", par_char_freq("aa aaa".as_bytes()));
        println!("{:?}", par_char_freq("".as_bytes()));
        println!("{:?}", par_char_freq("123ab1bc".as_bytes()));
    }
}
