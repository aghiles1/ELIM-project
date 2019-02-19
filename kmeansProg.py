import numpy as np
import matplotlib.pyplot as plt
from scipy.cluster.vq import kmeans2, whiten
import hdbscan


from sklearn.cluster import KMeans



import seaborn as sns
import sklearn.datasets as data

coordinates= np.array([
           [43.741566,7.336638],
           [43.740698,7.340842],
           [43.744108,7.335008],
           [43.745100,7.342086],
           [43.741684,7.342950],
           [43.741358,7.335593],
           [43.743807,7.333287],
           [43.743053,7.337476],

           [43.740741,7.022455],
           [43.739687,7.015506],
           [43.737454,7.034209],
           [43.739954,7.020924],
           [43.734558,7.024227],
           [43.740729,7.026414],
           [43.735985,7.024098],
           [43.742874,7.030963],
           [43.734937,7.020153],

           [43.925618,7.349110],
           [43.927040,7.358461],
           [43.926175,7.369100],
           [43.924197,7.353056],
           [43.916901,7.350740],
           [43.917581,7.361893],
           [43.931738,7.339415],
           [43.932814,7.353167],
           [43.924530,7.359000]
           ])
# x, y = kmeans2(whiten(coordinates), 3, iter = 20)
# plt.scatter(coordinates[:,0], coordinates[:,1], c=y);
# plt.show()

# rads = np.radians(coordinates)
# clusterer = hdbscan.HDBSCAN(min_cluster_size=2, metric='haversine')
# cluster_labels = clusterer.fit_predict(coordinates)
#
# clusterer.minimum_spanning_tree_.plot(edge_cmap='viridis',
#                                       edge_alpha=0.6,
#                                       node_size=80,
#                                       edge_linewidth=2)
k = 3
kmeans = KMeans(n_clusters=k)
kmeans.fit(coordinates)
y_kmeans = kmeans.predict(coordinates)

x, y = kmeans2(whiten(coordinates), k, iter = 20)
plt.scatter(coordinates[:,0], coordinates[:,1], c=y);
lists = [[] for _ in range(k)]

for i in range(0, len(coordinates)-1):
    lists[y[i]].append(coordinates[i]);
print lists
#centers = kmeans.cluster_centers_
#print centers
#plt.scatter(centers[:, 0], centers[:, 1], c='red', s=20, alpha=1);

plt.show()
