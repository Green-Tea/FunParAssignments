mod unsafeslice;

use crate::filter::unsafeslice::UnsafeSlice;
use bitvec::macros::internal::funty::Fundamental;
use rayon::iter::*;

#[allow(dead_code)]
pub fn par_filter<F>(xs: &[i32], p: F) -> Vec<i32>
where
    F: Fn(i32) -> bool + Send + Sync,
{
    if xs.is_empty() {
        return Vec::new();
    }
    let check: Vec<i32> = xs
        .into_par_iter()
        .map(|i| if p(i.clone()) { 1 } else { 0 })
        .clone()
        .collect::<Vec<i32>>();

    let (mut index, c_sum) = plus_scan(check.clone());

    if index.len() % 2 != 0 {
        index.push(c_sum);
    }

    unsafe {
        let mut res: Vec<i32> = Vec::with_capacity(c_sum.as_usize());
        res.set_len(c_sum.as_usize());
        let random_variable_name = UnsafeSlice::new(&mut res);
        check.into_par_iter().enumerate().for_each(|i| {
            if i.1 == 1 {
                random_variable_name.write(index[i.0] as usize, xs[i.0])
            }
        });
        res
    }
}

fn plus_scan(xs: Vec<i32>) -> (Vec<i32>, i32) {
    use rayon::iter::*;
    if xs.is_empty() {
        return (vec![], 0);
    }
    let half = xs.len() / 2;
    let (c_prefix, mut c_sum) = plus_scan(
        (0..half)
            .into_par_iter()
            .map(|i| xs[2 * i] + xs[2 * i + 1])
            .collect::<Vec<i32>>(),
    );
    let mut pfs: Vec<i32> = (0..half)
        .into_par_iter()
        .flat_map(|i| vec![c_prefix[i], c_prefix[i] + xs[2 * i]])
        .collect();
    if xs.len() % 2 == 1 {
        pfs.push(c_sum);
        c_sum += xs[xs.len() - 1];
    }

    (pfs, c_sum)
}

fn is_odd(x: i32) -> bool {
    if x % 2 == 0 {
        false
    } else {
        true
    }
}

fn is_even(x: i32) -> bool {
    if x % 2 == 0 {
        true
    } else {
        false
    }
}

#[cfg(test)]
mod tests {
    use crate::filter::{is_even, is_odd, par_filter};

    #[test]
    fn testing() {
        assert_eq!(
            vec![1, 3, 5, 7, 9],
            par_filter(&[1, 2, 3, 4, 5, 6, 7, 8, 9, 10], is_odd)
        );
        assert_eq!(
            vec![2, 4, 6, 8, 10],
            par_filter(&[1, 2, 3, 4, 5, 6, 7, 8, 9, 10], is_even)
        );
    }
}
