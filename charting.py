import matplotlib as mpl
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import matplotlib.pyplot as plt


def create_line_chart():

    observation_lengths = [5, 10, 15, 20, 30, 40, 50, 60]
    colors = ['red', 'orange', 'yellow', 'green', 'blue', 'purple', 'brown', 'gray', 'black']
    actual_churn_data = "churns.csv"
    algos = {'smas': {'name': 'Simple Moving Average', 'file_path': 'smas.csv'},
             'emas': {'name': 'Exponential Moving Average', 'file_path': 'emas.csv'},
             'demas': {'name': 'Dynamic Exponential Moving Average', 'file_path': 'demas.csv'}
            }
    churn_data = np.genfromtxt(actual_churn_data, delimiter=',', dtype=float)
    for algo in algos.keys():
        fig = plt.figure()
        ax = fig.add_subplot(111)
        churn_x = [i for i in xrange(len(churn_data))]
        ax.plot(churn_x, churn_data, color=colors[-1], label='Actual Churn')
        algo_data = np.genfromtxt(algos.get(algo, {}).get('file_path', ''), delimiter=',', dtype=float)
        for index, ol in enumerate(observation_lengths):
            x = [i for i in xrange(len(algo_data[index]))]
            ax.plot(x, algo_data[index], color=colors[index], label=algos.get(algo, {}).get('name', '') + ' OL ' + str(ol))
        lgd = ax.legend(loc="upper left", bbox_to_anchor=(1,1))
        ax.set_xlabel('Churn Interval')
        ax.set_ylabel('Number of Peers')
        fig.savefig(algo + '_ol.pdf', bbox_extra_artists=(lgd,), bbox_inches='tight')


def create_2d_bar_chart():
    rf_path = "rf_accuracy.csv"
    data = np.genfromtxt(rf_path, delimiter=',', dtype=float)

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    for c, z in zip(['r', 'g', 'b'], [0, 1, 2]):
        ys = data[z]
        xs = [i for i in xrange(len(ys))]
        cs = [c] * len(ys)
        ax.bar(xs, ys, zs=z, zdir='y', color=cs, alpha=0.8,)

    ax.set_xlabel('Observation Length')
    ax.set_ylabel('Prediction Algorithm')
    ax.set_zlabel('RF Accuracy')
    ax.set_yticks([0, 1, 2])
    ax.set_yticklabels(['SMA', 'EMA', 'DEMA'])
    fig.savefig('rf_accuracy.pdf')


if __name__ == "__main__":
    create_line_chart()
    create_2d_bar_chart()
