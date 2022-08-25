use num_bigint::{ToBigUint};

#[allow(dead_code)]
pub fn par_fib_seq(n: u32) -> Vec<num_bigint::BigUint> {
    let a1 = (1, 1, 1, 0);

    if n == 0 {
        vec![]
    } else if n == 1 {
        vec![1.to_biguint().unwrap()]
    } else {
        let mut temp = Vec::new();
        (1..n).into_iter().for_each(|_| temp.push(a1));
        let (mut mat, _base) = prefix_scan(temp);

        mat.push(a1);
        let mut res: Vec<num_bigint::BigUint> =
            mat.into_iter().map(|x| x.0.to_biguint().unwrap()).collect();
        res.sort();
        res
    }
}

pub fn multiply(f: (i32, i32, i32, i32), m: (i32, i32, i32, i32)) -> (i32, i32, i32, i32) {
    (
        (f.0 * m.0 + f.1 * m.2),
        (f.0 * m.1 + f.1 * m.3),
        (f.2 * m.0 + f.3 * m.2),
        (f.2 * m.1 + f.3 * m.3),
    )
}

pub fn prefix_scan(
    xs: Vec<(i32, i32, i32, i32)>,
) -> (Vec<(i32, i32, i32, i32)>, (i32, i32, i32, i32)) {
    use rayon::iter::*;
    if xs.is_empty() {
        return (vec![], (1, 1, 1, 0));
    }
    let half = xs.len() / 2;
    let (c_prefix, mut c_sum) = prefix_scan(
        (0..half)
            .into_par_iter()
            .map(|i| multiply(xs[2 * i], xs[2 * i + 1]))
            .collect::<Vec<(i32, i32, i32, i32)>>(),
    );

    let mut pfs: Vec<(i32, i32, i32, i32)> = (0..half)
        .into_par_iter()
        .flat_map(|i| vec![c_prefix[i], multiply(c_prefix[i], xs[2 * i])])
        .collect();

    if xs.len() % 2 == 1 {
        pfs.push(c_sum);
        c_sum = multiply(c_sum, xs[xs.len() - 1]);
    }

    (pfs, c_sum)
}

/*
Work = O(n) <= multiply
Span = O(log^2n)
 */

#[cfg(test)]
mod tests {
    use crate::fib::par_fib_seq;
    use num_bigint::ToBigUint;

    #[test]
    fn testing() {
        assert_eq!(
            vec![
                1.to_biguint().unwrap(),
                1.to_biguint().unwrap(),
                2.to_biguint().unwrap(),
                3.to_biguint().unwrap(),
                5.to_biguint().unwrap(),
                8.to_biguint().unwrap(),
                13.to_biguint().unwrap()
            ],
            par_fib_seq(7)
        );
    }
}
