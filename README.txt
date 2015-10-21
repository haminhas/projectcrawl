this file contains information for our team.



Guidelines:
1. NEVER "push --force" your code on any branch unless you spoken with all the other members of the team

2. Avoid as much as you can to push code on master. Master should always be the latest working version that is updated only through MERGE of other branches into master. To work on the project please follow this procedure:
	2.1 Go on branch master or the one from which you need to implement the new feature (but it should always be master);
	2.2 Pull the latest changes (git pull origin master), so that you create the new branch from the latest version of master.
	2.3 create a new branch (git checkout -b name-of-the-branch)
	2.4 Do all your changes here and to upload your code on the repository push the new branch (git push origin name-of-the-branch)
	2.5 If the changed go beyond 20 lines, or it’s not straight away clear what your code does, please create a PULL REQUEST (pull request is not related to git pull command), explaining what the feature of this branch does (ACCEPTANCE CRITERIA), and how to verify that your code works (TEST).

3. Before merging your branch into another branch, (ESPECIALLY WHEN MERGING ON MASTER), please ensure you have REBASED. so that you don’t delete new modifications that other people have done on that branch. Rebase might be complex at the beginning, it’s a good thing to do it together in person. 

4. Each different branch should be developed on a different branch with an appropriate name.


