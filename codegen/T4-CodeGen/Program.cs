using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace T4_CodeGen
{
    class Program
    {
        static void Main(string[] args)
        {
        }

        string[] GetIgnoreColumns(string tableName)
        {
            string[] tables1 = new string[] { "tx_audit", "tx_ddl", "tx_pos", "tx_amount_breakdown", "tx_ft_app", "tx_user" };
            if (tables1.Contains(tableName))
                return new string[] { };            
            string[] ignoreColumns = new string[] { "order_id", "order_no", "pos_no", "payment_status", "merchant_id", "outlet_id", "txdate", "txtime", "txtimestamp", "txts" };
            List<string> ignoreColumnsList = new List<string>(ignoreColumns);
            switch (tableName)
            {
                case "tx_block":
                    ignoreColumnsList.Add("id");
                    ignoreColumnsList.Add("block_id");
                    break;
                default:
                    break;
            }
            return ignoreColumnsList.ToArray();
        }
    }
}
