import matplotlib as mpl
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import matplotlib.pyplot as plt


def create_line_chart():

    observation_lengths = [10, 20, 40, 60]
    colors = ['red', 'orange', 'yellow', 'green', 'blue', 'purple', 'brown', 'gray', 'black']
    actual_churn_data = "outputExample/churns.csv"
    algos = {'smas': {'name': 'Simple Moving Average', 'file_path': 'outputExample/smas.csv'},
             'emas': {'name': 'Exponential Moving Average', 'file_path': 'outputExample/emas.csv'},
             'demas': {'name': 'Dynamic Exponential Moving Average', 'file_path': 'outputExample/demas.csv'},
             'pidfe': {'name': 'PID Feedback Estimator', 'file_path': 'outputExample/pidfe.csv'}
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


def comparison_ol_chart():
    observation_lengths = [10, 20, 40, 60]
    start = 270
    end = 459
    actual_churn_data = "outputExample/churns.csv"
    algos = {'smas': {'name': 'Simple Moving Average', 'file_path': 'outputExample/smas.csv', 'color': 'red'},
             'emas': {'name': 'Exponential Moving Average', 'file_path': 'outputExample/emas.csv', 'color': 'green'},
             'demas': {'name': 'Dynamic Exponential Moving Average', 'file_path': 'outputExample/demas.csv', 'color': 'orange'},
             'pidfe': {'name': 'PID Feedback Estimator', 'file_path': 'outputExample/pidfe.csv', 'color': 'purple'}
             }
    churn_data = np.genfromtxt(actual_churn_data, delimiter=',', dtype=float)
    churn_data = churn_data[start:end+1]
    churn_x = [i for i in xrange(len(churn_data))]

    for algo in algos.keys():
        algo_data = np.genfromtxt(algos.get(algo, {}).get('file_path', ''), delimiter=',', dtype=float)
        algos[algo]['data'] = algo_data

    for index, ol in enumerate(observation_lengths):
        fig = plt.figure()
        ax = fig.add_subplot(111)
        ax.plot(churn_x, churn_data, color='black', label='Actual Churn')
        for algo in algos.keys():
            current_data = algos[algo].get('data', [])[index]
            x = [i for i in xrange(len(current_data[start:end+1]))]
            ax.plot(x, current_data[start:end+1], color=algos[algo].get('color', ''), label=algos.get(algo, {}).get('name', ''))
        lgd = ax.legend(loc="upper left", bbox_to_anchor=(1, 1))
        ax.set_xlabel('Churn Interval')
        ax.set_ylabel('Number of Peers')
        ax.set_title('Comparison of 4 Models for OL {0}'.format(ol))
        fig.savefig(str(ol) + '_ol.pdf', bbox_extra_artists=(lgd,), bbox_inches='tight')


def create_2d_rf_bar_chart():
    rf_path = "outputExample/rfAccuracy.csv"
    data = np.genfromtxt(rf_path, delimiter=',', dtype=float)

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    for c, z in zip(['r', 'g', 'b', 'black'], [0, 1, 2, 3]):
        ys = data[z]
        xs = [i for i in xrange(len(ys))]
        cs = [c] * len(ys)
        ax.bar(xs, ys, zs=z, zdir='y', color=cs, alpha=0.8,)

    ax.set_xlabel('Observation Length')
    ax.set_ylabel('Prediction Algorithm')
    ax.set_zlabel('RF Accuracy')
    ax.set_yticks([0, 1, 2])
    ax.set_yticklabels(['SMA', 'EMA', 'DEMA', 'PIDFE'])
    fig.savefig('rf_accuracy.pdf')


def create_2d_wp_bar_chart():
    rf_path = "outputExample/winPercent.csv"
    data = np.genfromtxt(rf_path, delimiter=',', dtype=float)

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    for c, z in zip(['r', 'g', 'b', 'black'], [0, 1, 2, 3]):
        ys = data[z]
        xs = [i for i in xrange(len(ys))]
        cs = [c] * len(ys)
        ax.bar(xs, ys, zs=z, zdir='y', color=cs, alpha=0.8,)

    ax.set_xlabel('Observation Length')
    ax.set_ylabel('Prediction Algorithm')
    ax.set_zlabel('Proportion of Wins')
    ax.set_yticks([0, 1, 2])
    ax.set_yticklabels(['SMA', 'EMA', 'DEMA'])
    fig.savefig('wp.pdf')


if __name__ == "__main__":
    create_line_chart()
    create_2d_rf_bar_chart()
    create_2d_wp_bar_chart()
    comparison_ol_chart()

