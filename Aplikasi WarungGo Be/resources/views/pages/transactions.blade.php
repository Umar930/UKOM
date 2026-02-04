<x-layouts.app title="Transaksi">
    <!-- Page Header -->
    <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Transaksi</h1>
            <p class="mt-1 text-sm text-gray-500">Riwayat semua transaksi penjualan.</p>
        </div>
        <div class="flex items-center gap-3">
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                </svg>
                Export Excel
            </x-button>
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"></path>
                </svg>
                Print
            </x-button>
        </div>
    </div>

    <!-- Filters Card -->
    <x-card class="mb-6">
        <div class="flex flex-col lg:flex-row gap-4">
            <div class="flex-1">
                <x-search-input placeholder="Cari berdasarkan Invoice ID, nama pelanggan..." />
            </div>
            <div class="flex flex-wrap items-center gap-3">
                <!-- Date Range -->
                <div class="flex items-center gap-2">
                    <x-input type="date" name="date_from" placeholder="Dari" />
                    <span class="text-gray-400">-</span>
                    <x-input type="date" name="date_to" placeholder="Sampai" />
                </div>
                
                <x-select 
                    name="status" 
                    :options="[
                        'all' => 'Semua Status',
                        'success' => 'Sukses',
                        'pending' => 'Pending',
                        'cancelled' => 'Dibatalkan'
                    ]"
                    selected="all"
                    placeholder=""
                />
                
                <x-select 
                    name="payment_method" 
                    :options="[
                        'all' => 'Semua Metode',
                        'cash' => 'Cash',
                        'qris' => 'QRIS',
                        'transfer' => 'Transfer',
                        'debit' => 'Kartu Debit'
                    ]"
                    selected="all"
                    placeholder=""
                />
                
                <x-button variant="secondary">
                    <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"></path>
                    </svg>
                    Filter
                </x-button>
            </div>
        </div>
    </x-card>

    <!-- Summary Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Total Transaksi Hari Ini</p>
                    <p class="text-2xl font-bold text-gray-900 font-mono mt-1">0</p>
                </div>
                <div class="p-3 bg-emerald-100 rounded-xl">
                    <svg class="w-6 h-6 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                    </svg>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Pendapatan Hari Ini</p>
                    <p class="text-2xl font-bold text-gray-900 font-mono mt-1">Rp 0</p>
                </div>
                <div class="p-3 bg-blue-100 rounded-xl">
                    <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Transaksi Sukses</p>
                    <p class="text-2xl font-bold text-green-600 font-mono mt-1">0</p>
                </div>
                <div class="p-3 bg-green-100 rounded-xl">
                    <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Dibatalkan</p>
                    <p class="text-2xl font-bold text-red-600 font-mono mt-1">0</p>
                </div>
                <div class="p-3 bg-red-100 rounded-xl">
                    <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
            </div>
        </div>
    </div>

    <!-- Transactions Table -->
    <x-card :padding="false">
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header">Invoice ID</x-table-cell>
                <x-table-cell type="header">Tanggal & Waktu</x-table-cell>
                <x-table-cell type="header">Pelanggan</x-table-cell>
                <x-table-cell type="header">Item</x-table-cell>
                <x-table-cell type="header">Metode Bayar</x-table-cell>
                <x-table-cell type="header" align="right">Total</x-table-cell>
                <x-table-cell type="header" align="center">Status</x-table-cell>
                <x-table-cell type="header" align="center">Aksi</x-table-cell>
            </x-slot>

            <!-- Empty State -->
            <tr>
                <td colspan="8" class="px-6 py-16 text-center">
                    <div class="flex flex-col items-center justify-center">
                        <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mb-4">
                            <svg class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                            </svg>
                        </div>
                        <p class="text-gray-600 font-medium text-lg">Belum ada transaksi</p>
                        <p class="text-sm text-gray-400 mt-1 max-w-sm">Transaksi akan muncul di sini setelah Anda melakukan penjualan</p>
                    </div>
                </td>
            </tr>
        </x-table>
    </x-card>

    <!-- Transaction Detail Modal -->
    <x-modal id="transaction-detail" title="Detail Transaksi" size="lg">
        <div class="text-center py-8 text-gray-500">
            Pilih transaksi untuk melihat detail
        </div>
    </x-modal>
</x-layouts.app>
