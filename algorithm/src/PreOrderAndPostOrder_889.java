import java.util.Arrays;

/**
 * 889. 根据前序和后序遍历构造二叉树
 */
public class PreOrderAndPostOrder_889 {

    public TreeNode constructFromPrePost(int[] pre, int[] post) {
        if (pre.length == 0 || pre.length != post.length) return null;
        TreeNode treeNode = new TreeNode(pre[0]);
        if (pre.length == 1) return treeNode;
        int i = pre[1];
        int index=0;
        for (int i1 = 0; i1 < post.length; i1++) {
            if (i==post[i1]) index = i1+1;
        }
        treeNode.left = constructFromPrePost(Arrays.copyOfRange(pre,1,index+1),
                Arrays.copyOfRange(post,0,index));
        treeNode.right = constructFromPrePost(Arrays.copyOfRange(pre,index+1,pre.length),
                Arrays.copyOfRange(post,index,post.length-1));
        return treeNode;
    }


    /**
     * Definition for a binary tree node.
     */
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
        int[] a = new int[3];
        a[0] = 1;
        a[1] = 2;
        a[2] = 3;
        System.out.println(Arrays.toString(Arrays.copyOfRange(a, 2, 2)));
    }
}
