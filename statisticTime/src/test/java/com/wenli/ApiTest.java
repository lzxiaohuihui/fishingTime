package com.wenli;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiTest {

    //    @Test
    //    public void testApi(){
    //        HashSet<String> set = new HashSet<>();
    //        set.add("avc");
    //        set.add("aff");
    //        set.add("aer");
    //        Map<String, Integer> map = new HashMap<>();
    //        set.stream().forEach(v ->{
    //            map.put(v, map.size());
    //        });
    //        Arrays.fill();
    //        System.out.println("he");
    //
    //    }
    @Test
    public void testApi() {
        char[][] board = new char[][]{{'O', 'X', 'X', 'O', 'X'}, {'X', 'O', 'O', 'X', 'O'}, {'X', 'O', 'X', 'O', 'X'}
                , {'O', 'X', 'O', 'O', 'O'}, {'X', 'X', 'O', 'X', 'O'}};
        for (char[] chars : board) {
            System.out.println(Arrays.toString(chars));
        }
        System.out.println("==============");
        solve(board);
        for (char[] chars : board) {
            System.out.println(Arrays.toString(chars));
        }
    }

    public void solve(char[][] board) {
        int m = board.length, n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (board[i][j] == 'X') continue;
                if (visited[i][j]) continue;
                List<List<Integer>> path = new ArrayList();
                boolean b = dfs(board, visited, m, n, i, j, path);
                if (b) continue;
                for (List<Integer> p : path) {
                    board[p.get(0)][p.get(1)] = 'X';
                }
            }
        }

    }

    boolean dfs(char[][] board, boolean[][] visited, int m, int n, int i, int j, List<List<Integer>> path) {
        if (i <= 0 || j <= 0) return true;
        if (i >= m - 1 || j >= n - 1) return true;
        if (visited[i][j]) return false;
        if (board[i][j] == 'X') return false;
        visited[i][j] = true;
        path.add(Arrays.asList(i, j));
        boolean b1 = dfs(board, visited, m, n, i - 1, j, path) && board[i - 1][j] == 'O';
        boolean b2 = dfs(board, visited, m, n, i + 1, j, path) && board[i + 1][j] == 'O';
        boolean b3 = dfs(board, visited, m, n, i, j - 1, path) && board[i][j - 1] == 'O';
        boolean b4 = dfs(board, visited, m, n, i, j + 1, path) && board[i][j + 1] == 'O';
        return b1 || b2 || b3 || b4;
    }





}
