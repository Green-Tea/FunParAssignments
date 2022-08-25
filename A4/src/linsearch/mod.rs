#[allow(dead_code)]
pub fn par_lin_search<T: Eq + Sync>(xs: &[T], k: &T) -> Option<usize> {
    if xs.len() <= 1 {
        Some(0)
    } else {
        let (left, right) = xs.split_at(xs.len() / 2);
        let (left_res, right_res) =
            rayon::join(|| helper(0, left, k), || helper(left.len(), right, k));
        if left_res == None {
            right_res
        } else {
            left_res
        }
    }
}

fn helper<T: Eq + Sync>(left: usize, xs: &[T], k: &T) -> Option<usize> {
    for (x, y) in xs.iter().enumerate() {
        if *y == *k {
            return Some(x + left);
        }
    }
    None
}

#[cfg(test)]
mod tests {
    use crate::linsearch::par_lin_search;

    #[test]
    fn basic_lin_search() {
        let xs = vec![3, 1, 4, 2, 7, 3, 1, 9];
        assert_eq!(par_lin_search(&xs, &3), Some(0));
        assert_eq!(par_lin_search(&xs, &5), None);
        assert_eq!(par_lin_search(&xs, &1), Some(1));
        assert_eq!(par_lin_search(&xs, &2), Some(3));

        let ys = vec![10, 100, 99, 123, 1, 1, 1, 15, 2, 4, 199];
        assert_eq!(par_lin_search(&ys, &1), Some(4));
        assert_eq!(par_lin_search(&ys, &1000), None);
        assert_eq!(par_lin_search(&ys, &15), Some(7));
        assert_eq!(par_lin_search(&ys, &199), Some(10));
    }
}
